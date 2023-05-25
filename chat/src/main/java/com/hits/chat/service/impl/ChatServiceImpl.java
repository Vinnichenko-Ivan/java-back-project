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
import java.util.*;
import java.util.stream.Collectors;

import static com.hits.common.Paths.*;

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

        try {
            Utils.logExternalQuery(USER_SERVICE_NAME, USER_CHECK_USER);
            if(!userService.checkUser(sendMessageDto.getChatId(), apiKeyProvider.getKey())) {
                throw new NotFoundException("user not found");
            }
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ExternalServiceErrorException("user service error");
        }

        if(jwtProvider.getId().equals(sendMessageDto.getChatId())) {
            throw new BadRequestException("message to youself");
        }

        Chat chat = chatRepository.getPrivateChat(jwtProvider.getId(), sendMessageDto.getChatId());
        if(chat == null) {
            Set<UUID> users = new HashSet<>(Set.of(jwtProvider.getId(), sendMessageDto.getChatId()));
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
        message = messageRepository.save(message);
        chat.getMessages().add(message);
        chat = chatRepository.save(chat);

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

//        chatRepository.save(chat);//TODO получение данных о личных чатах
    }

    @Override
    public ChatInfoDto getChatInfo(UUID id) {
        try{
            Chat chat = chatRepository.findById(id).orElseThrow(() -> new NotFoundException("chat not fount"));
            if(!chat.getUsers().contains(jwtProvider.getId())) {
                throw new NotFoundException("chat not fount");
            }
            return chatMapper.map(
                    chat
            );
        } catch (NotFoundException e) {
            Chat chat = chatRepository.getPrivateChat(jwtProvider.getId(), id);
            if(chat == null) {
                throw e;
            }
            if(!chat.getUsers().contains(jwtProvider.getId())) {
                throw new NotFoundException("chat not fount");
            }
            ChatInfoDto chatInfoDto = chatMapper.map(chat);
            chatInfoDto.setName(getChatName(chat));
            chatInfoDto.setDateCreated(chat.getCreatedDate());//TODO аватарка пользователя
            return chatInfoDto;
        }
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
    public List<MessageDto> getMessages(UUID id) {//TODO картинка
        Chat chat = getChat(id);
        List<Message> messages = chat.getMessages();

        var dtos = messageMapper.map(messages);

        for (int i = 0; i < messages.size(); i++) {
            FullNameDto fullNameDto;
            try {
                Utils.logExternalQuery(USER_SERVICE_NAME, USER_USER_NAME);
                fullNameDto = userService.getUserName(messages.get(i).getAuthorId(), apiKeyProvider.getKey());
            }catch (Exception e) {
                throw new ExternalServiceErrorException("user service error");
            }
            dtos.get(i).setAuthorName(fullNameDto);
        }

        return dtos;
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
        users.add(id);
        users.forEach((user) -> {
            try {
                Utils.logExternalQuery(USER_SERVICE_NAME, USER_CHECK_USER);
                if(!userService.checkUser(user, apiKeyProvider.getKey())) {
                    throw new NotFoundException("user not found");
                }
            }
            catch (NotFoundException e) {
                throw e;
            }
            catch (Exception e) {
                throw new ExternalServiceErrorException("user service error");
            }
        });
        return users;
    }

    @Override
    public List<MessageFindDto> getMessages(String find) {
        List<Message> messages = messageRepository.getMessages(jwtProvider.getId(), find.toLowerCase());
        return messages.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ChatFindInfoDto toDto(Chat chat) {
        ChatFindInfoDto chatFindInfoDto = new ChatFindInfoDto();
        chatFindInfoDto.setId(chat.getId());
        chatFindInfoDto.setName(getChatName(chat));

        Message message = chat.getLastMessageId() != null ? messageRepository.findById(chat.getLastMessageId()).orElse(null) : null;

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
                Utils.logExternalQuery(USER_SERVICE_NAME, USER_USER_NAME);
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
                if(!myId.equals(id)) {
                    userId = id;
                    break;
                }
            }
            FullNameDto fullNameDto;
            if(userId == null) {
                throw new IllegalStateException();
            }
            try {
                Utils.logExternalQuery(USER_SERVICE_NAME, USER_USER_NAME);
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

    private Chat getChat(UUID id) {
        try{
            Chat chat = chatRepository.findById(id).orElseThrow(() -> new NotFoundException("chat not fount"));
            if(!chat.getUsers().contains(jwtProvider.getId())) {
                throw new NotFoundException("chat not fount");
            }
            return chat;
        } catch (NotFoundException e) {
            Chat chat = chatRepository.getPrivateChat(jwtProvider.getId(), id);
            if(chat == null) {
                throw e;
            }
            return chat;
        }
    }
}
