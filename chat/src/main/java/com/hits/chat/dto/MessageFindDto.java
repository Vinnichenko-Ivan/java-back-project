package com.hits.chat.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class MessageFindDto {
    private UUID chatId;
    private String chatName;
    private String text;
    private Date dateCreated;
    private List<String> files;
}
