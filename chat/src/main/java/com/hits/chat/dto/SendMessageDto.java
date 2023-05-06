package com.hits.chat.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class SendMessageDto {
    private UUID chatId;
    private String text;
    private Set<FileDto> files;
}
