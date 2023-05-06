package com.hits.chat.controller;

import com.hits.chat.dto.*;
import com.hits.chat.service.ChatService;
import com.hits.common.exception.NotImplementedException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Api
@RestController
@RequestMapping(value = "/chat/chat", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class ChatController {
    private final ChatService chatService;

    @PostMapping(value = "/private/message")
    public void sendPrivateMessage(@Valid @RequestBody SendMessageDto sendMessageDto) {
        chatService.sendPrivateMessage(sendMessageDto);
    }

    @PostMapping(value = "/")
    public void createChat(@Valid @RequestBody CreateChatDto createChatDto) {
        chatService.createChat(createChatDto);
    }

    @PutMapping(value = "/")
    public void editChat(@Valid @RequestBody EditChatDto editChatDto) {
        chatService.editChat(editChatDto);
    }

    @PostMapping(value = "/message")
    public void sendMessage(@Valid @RequestBody SendMessageDto sendMessageDto) {
        chatService.sendMessage(sendMessageDto);
    }

    @GetMapping(value = "/{id}")
    public ChatInfoDto getChatInfo(@PathVariable UUID id) {
        return chatService.getChatInfo(id);
    }

    @PostMapping(value = "/find")
    public List<ChatFindInfoDto> findChatInfo(@Valid @RequestBody ChatQueryDto chatQueryDto) {
        return chatService.findChatInfo(chatQueryDto);
    }

    @GetMapping(value = "/{id}/message")
    public List<MessageDto> getMessages(@PathVariable UUID id) {
        return chatService.getMessages(id);
    }
}
