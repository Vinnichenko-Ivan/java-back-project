package com.hits.chat.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class EditChatDto {
    private UUID chatId;
    private String name;
    private UUID avatarId;
    private Set<UUID> usersId;
}
