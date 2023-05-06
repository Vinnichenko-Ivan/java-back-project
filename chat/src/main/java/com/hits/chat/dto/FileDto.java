package com.hits.chat.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class FileDto {
    private UUID fileId;

    private String fileName;
}
