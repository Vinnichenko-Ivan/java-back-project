package com.hits.chat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
public class SendMessageDto {
    @NotNull
    private UUID chatId;
    @NotNull
    private String text;
    private Set<FileDto> files;
}
