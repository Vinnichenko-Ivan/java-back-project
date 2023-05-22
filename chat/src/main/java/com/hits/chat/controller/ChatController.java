package com.hits.chat.controller;

import com.hits.chat.dto.*;
import com.hits.chat.service.ChatService;
import com.hits.common.exception.NotImplementedException;
import com.hits.common.service.Utils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.hits.common.Paths.*;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Log4j2
public class ChatController {
    private final ChatService chatService;

    @PostMapping(value = CHAT_SEND_PRIVATE_MESSAGE)
    public void sendPrivateMessage(@Valid @RequestBody SendMessageDto sendMessageDto) {
        Utils.logQuery(CHAT_SEND_PRIVATE_MESSAGE, sendMessageDto);
        chatService.sendPrivateMessage(sendMessageDto);
    }

    @PostMapping(value = CHAT_CREATE)
    public void createChat(@Valid @RequestBody CreateChatDto createChatDto) {
        Utils.logQuery(CHAT_CREATE, createChatDto);
        chatService.createChat(createChatDto);
    }

    @PutMapping(value = CHAT_EDIT)
    public void editChat(@Valid @RequestBody EditChatDto editChatDto) {
        Utils.logQuery(CHAT_EDIT, editChatDto);
        chatService.editChat(editChatDto);
    }

    @PostMapping(value = CHAT_SEND_MESSAGE)
    public void sendMessage(@Valid @RequestBody SendMessageDto sendMessageDto) {
        Utils.logQuery(CHAT_SEND_MESSAGE, sendMessageDto);
        chatService.sendMessage(sendMessageDto);
    }

    @GetMapping(value = CHAT_INFO)
    public ChatInfoDto getChatInfo(@PathVariable UUID id) {
        Utils.logQuery(CHAT_INFO, id);
        return chatService.getChatInfo(id);
    }

    @PostMapping(value = CHAT_FIND)
    public ChatFindInfoPagDto findChatInfo(@Valid @RequestBody ChatQueryDto chatQueryDto) {
        Utils.logQuery(CHAT_FIND, chatQueryDto);
        return chatService.findChatInfo(chatQueryDto);
    }

    @GetMapping(value = CHAT_GET_MESSAGES)
    public List<MessageDto> getMessages(@PathVariable UUID id) {
        Utils.logQuery(CHAT_GET_MESSAGES, id);
        return chatService.getMessages(id);
    }

    @GetMapping(value = CHAT_FIND_MESSAGES)
    public List<MessageFindDto> findMessages(@RequestParam String find) {
        Utils.logQuery(CHAT_FIND_MESSAGES, find);
        return chatService.getMessages(find);
    }
}
