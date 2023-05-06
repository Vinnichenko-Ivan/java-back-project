package com.hits.chat.controller;

import com.hits.chat.dto.*;
import com.hits.common.exception.NotImplementedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/chat/chat", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class ChatController {

    @PostMapping(value = "/private/message")
    public void sendPrivateMessage(@Valid @RequestBody SendMessageDto sendMessageDto) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/")
    public void createChat(@Valid @RequestBody CreateChatDto createChatDto) {
        throw new NotImplementedException();
    }

    @PutMapping(value = "/")
    public void editChat(@Valid @RequestBody CreateChatDto createChatDto) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/message")
    public void sendMessage(@Valid @RequestBody SendMessageDto sendMessageDto) {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/{id}")
    public ChatInfoDto getChatInfo(@PathVariable UUID id) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/find")
    public List<ChatFindInfoDto> findChatInfo(@Valid @RequestBody ChatQueryDto chatQueryDto) {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/{id}/message")
    public List<MessageDto> getMessages(@PathVariable UUID id) {
        throw new NotImplementedException();
    }
}
