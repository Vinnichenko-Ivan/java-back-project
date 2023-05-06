package com.hits.chat.service.impl;

import com.hits.chat.dto.*;
import com.hits.chat.mapper.ChatMapper;
import com.hits.chat.mapper.MessageMapper;
import com.hits.chat.model.Chat;
import com.hits.chat.model.ChatType;
import com.hits.chat.model.File;
import com.hits.chat.model.Message;
import com.hits.chat.repository.ChatRepository;
import com.hits.chat.repository.MessageRepository;
import com.hits.chat.service.UserService;
import com.hits.chat.service.ChatService;
import com.hits.common.exception.NotFoundException;
import com.hits.common.exception.NotImplementedException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.JwtProvider;
import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional
    public void sendPrivateMessage(SendMessageDto sendMessageDto) {
        if(!userService.checkUser(sendMessageDto.getChatId(), apiKeyProvider.getKey())) {
            throw new NotFoundException("user not found");
        }
        Set<UUID> users = Set.of(jwtProvider.getId(), sendMessageDto.getChatId());
        Chat chat = chatRepository.getChatByChatTypeAndUsers(ChatType.PRIVATE, users);
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
        messageRepository.save(message);
    }

    @Override
    public ChatInfoDto getChatInfo(UUID id) {
        return chatMapper.map(
                chatRepository.findById(id).orElseThrow(() -> new NotFoundException("chat not fount"))
        );
    }

    @Override
    public List<ChatFindInfoDto> findChatInfo(ChatQueryDto chatQueryDto) {
        throw new NotImplementedException();
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

    private Set<File> saveFiles(Set<FileDto> fileDtos) {//map
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
}
