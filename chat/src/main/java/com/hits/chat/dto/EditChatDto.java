package com.hits.chat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
public class EditChatDto {
    @NotNull
    private UUID chatId;
    @NotNull
    private String name;
    private UUID avatarId;
    private Set<UUID> usersId;
}
