package com.hits.chat.dto;

import com.hits.chat.model.File;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class SendPrivateMessageDto {
    private UUID user;
    private String text;
    private Set<File> files;
}
