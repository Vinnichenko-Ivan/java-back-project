package com.hits.chat.dto;

import java.util.Set;
import java.util.UUID;

public class SendMessageDto {
    private UUID chatId;
    private String text;
    private Set<FileDto> files;
}
