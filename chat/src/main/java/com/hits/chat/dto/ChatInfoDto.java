package com.hits.chat.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ChatInfoDto {
    private String name;
    private UUID avatarId;
    private UUID adminId;
    private Date dateCreated;
}
