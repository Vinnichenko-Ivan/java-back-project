package com.hits.chat.service;

import com.hits.chat.dto.*;
import com.hits.common.exception.NotImplementedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface ChatService {
    void sendPrivateMessage(SendMessageDto sendMessageDto);
    void createChat(CreateChatDto createChatDto);
    void editChat(EditChatDto editChatDto);
    void sendMessage(SendMessageDto sendMessageDto);
    ChatInfoDto getChatInfo(UUID id);
    ChatFindInfoPagDto findChatInfo(ChatQueryDto chatQueryDto);
    List<MessageDto> getMessages(UUID id);

    List<MessageFindDto> getMessages(String find);
}
