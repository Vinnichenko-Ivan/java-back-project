package com.hits.chat.service.impl;

import com.hits.chat.dto.*;
import com.hits.chat.dto.MessageDto;
import com.hits.chat.mapper.ChatMapper;
import com.hits.chat.mapper.MessageMapper;
import com.hits.chat.model.*;
import com.hits.chat.repository.ChatRepository;
import com.hits.chat.repository.MessageRepository;
import com.hits.chat.service.NotificationRabbitProducer;
import com.hits.chat.service.UserService;
import com.hits.chat.service.ChatService;
import com.hits.common.dto.user.FullNameDto;
import com.hits.common.exception.*;
import com.hits.common.exception.IllegalStateException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.JwtProvider;
import com.hits.common.service.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    private final JwtProvider jwtProvider;

    private final MessageRepository messageRepository;

    private final ApiKeyProvider apiKeyProvider;

    private final UserService userService;

    private final ChatMapper chatMapper;

    private final MessageMapper messageMapper;

    private final NotificationRabbitProducer notificationRabbitProducer;


    @Override
    @Transactional
    public void sendPrivateMessage(SendMessageDto sendMessageDto) {
        if(!userService.checkUser(sendMessageDto.getChatId(), apiKeyProvider.getKey())) {
            throw new NotFoundException("user not found");
        }
        Set<UUID> users = Set.of(jwtProvider.getId(), sendMessageDto.getChatId());
        Chat chat = chatRepository.getPrivateChat(jwtProvider.getId(), sendMessageDto.getChatId());
        if(chat == null) {
            chat = createPrivateChat(users);
        }
        sendMessageDto.setChatId(chat.getId());

        sendMessage(sendMessageDto);
    }

    @Override
    public void createChat(CreateChatDto createChatDto) {
        Chat chat = new Chat();

        chat.setChatType(ChatType.CHAT);
        chat.setAdminUser(jwtProvider.getId());

        if(createChatDto.getUsersId().contains(jwtProvider.getId())) {
            throw new BadRequestException("admin in users");
        }

        chat.setUsers(toUsers(createChatDto.getUsersId(), jwtProvider.getId()));
        chat.setAvatarId(createChatDto.getAvatarId());
        chat.setName(createChatDto.getName());

        chatRepository.save(chat);
    }

    @Override
    public void editChat(EditChatDto editChatDto) {
        Chat chat = chatRepository.findById(editChatDto.getChatId()).orElseThrow(() -> new NotFoundException("chat not fount"));

        if(chat.getChatType() == ChatType.PRIVATE){
            throw new NotFoundException("chat not find");
        }

        chat.setUsers(toUsers(editChatDto.getUsersId(), jwtProvider.getId()));
        chat.setAvatarId(editChatDto.getAvatarId());
        chat.setName(editChatDto.getName());

        chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void sendMessage(SendMessageDto sendMessageDto) {
        UUID userId = jwtProvider.getId();
        Chat chat = chatRepository.findById(sendMessageDto.getChatId()).orElseThrow(() -> new NotFoundException("chat not fount"));
        if(!chat.getUsers().contains(userId)) {
            throw new NotFoundException("chat not found");
        }
        Message message = new Message();
        message.setAuthorId(userId);
        message.setText(sendMessageDto.getText());
        message.setFiles(saveFiles(sendMessageDto.getFiles()));
        message.setChat(chat);
        message = messageRepository.save(message);

        if(chat.getChatType() == ChatType.PRIVATE) { //что бы отправлять только в лс
            for(UUID id : chat.getUsers()) {
                if(id != userId) {
                    notificationRabbitProducer.sendNewMessageNotify(
                            sendMessageDto.getChatId(),
                            getChatName(chat),
                            sendMessageDto.getText()
                    );
                }
            }
        }
//        chat = chatRepository.findById(sendMessageDto.getChatId()).orElseThrow(() -> new NotFoundException("chat not fount"));
        chat.setLastMessageId(message.getId());

//        chatRepository.save(chat);
    }

    @Override
    public ChatInfoDto getChatInfo(UUID id) {
        return chatMapper.map(
                chatRepository.findById(id).orElseThrow(() -> new NotFoundException("chat not fount"))
        );
    }

    @Override
    public ChatFindInfoPagDto findChatInfo(ChatQueryDto chatQueryDto) {
        List<Sort.Order> orders = List.of(
                new Sort.Order(Sort.Direction.ASC, Chat_.createdDate.getName())
                );

        Pageable pageable = Utils.toPageable(chatQueryDto.getPaginationQueryDto(), orders);
        Page<Chat> chats = chatRepository.findAll(new ChatSpecification(chatQueryDto.getNameFilter(), jwtProvider.getId()), pageable);

        ChatFindInfoPagDto dto = new ChatFindInfoPagDto();
        dto.setChatFindInfoDtos(
                chats.stream().map(this::toDto).collect(Collectors.toList())
        );
        dto.setPaginationDto(Utils.toPagination(chats));
        return dto;
    }

    @Override
    public List<MessageDto> getMessages(UUID id) {
        return messageMapper.map(messageRepository.getAllByChat_Id(id));
    }

    private Chat createPrivateChat(Set<UUID> users) {
        Chat chat = new Chat();
        chat.setChatType(ChatType.PRIVATE);
        chat.setUsers(users);
        return chatRepository.save(chat);
    }

    private Set<File> saveFiles(Set<FileDto> fileDtos) {
        if(fileDtos == null)
        {
            return null;//map
        }
        return fileDtos.stream().map((f) -> new File(f.getFileId(), f.getFileName())).collect(Collectors.toSet());
    }

    private Set<UUID> toUsers(Set<UUID> users, UUID id) {
        users.forEach((user) -> {
            if(!userService.checkUser(user, apiKeyProvider.getKey())) {
                throw new NotFoundException("user " + user + " not found");
            }
        });
        users.add(id);
        return users;
    }

    @Override
    public List<MessageFindDto> getMessages(String find) {
        List<Message> messages = messageRepository.getMessages(jwtProvider.getId(), find);
        return messages.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ChatFindInfoDto toDto(Chat chat) {
        ChatFindInfoDto chatFindInfoDto = new ChatFindInfoDto();
        chatFindInfoDto.setId(chat.getId());
        chatFindInfoDto.setName(getChatName(chat));
        Message message = messageRepository.findById(chat.getLastMessageId()).orElse(null);

        if(message == null)
        {
            chatFindInfoDto.setLastMessageText(null);
            chatFindInfoDto.setLastMessageAuthor(null);
            chatFindInfoDto.setLastMessageDate(null);
        }
        else {
            chatFindInfoDto.setLastMessageText(message.getText());

            FullNameDto fullNameDto;
            try {
                fullNameDto = userService.getUserName(message.getAuthorId(), apiKeyProvider.getKey());
            }catch (Exception e) {
                throw new ExternalServiceErrorException("user service error");
            }
            chatFindInfoDto.setLastMessageAuthor(fullNameDto);
            chatFindInfoDto.setLastMessageDate(message.getCreatedDate());
        }

        return chatFindInfoDto;
    }



    private String getChatName(Chat chat) {
        if(chat.getChatType() == ChatType.PRIVATE)
        {
            UUID userId = null;
            UUID myId = jwtProvider.getId();

            Set<UUID> users = chat.getUsers();

            for(UUID id:users) {
                if(myId != id) {
                    userId = id;
                    break;
                }
            }
            FullNameDto fullNameDto;
            if(userId == null) {
                throw new IllegalStateException();
            }
            try {
                fullNameDto = userService.getUserName(userId, apiKeyProvider.getKey());
            }catch (Exception e) {
                throw new ExternalServiceErrorException("user service error");
            }
            return fullNameDto.toString();
        }
        else {
            return chat.getName();
        }
    }

    private MessageFindDto toDto(Message message) {
        MessageFindDto messageFindDto = new MessageFindDto();

        Chat chat = message.getChat();

        messageFindDto.setChatName(getChatName(chat));
        messageFindDto.setDateCreated(message.getCreatedDate());
        messageFindDto.setText(message.getText());
        messageFindDto.setChatId(chat.getId());
        messageFindDto.setFiles(message.getFiles().stream().map(f -> f.getFileName()).collect(Collectors.toList()));

        return messageFindDto;
    }
}
