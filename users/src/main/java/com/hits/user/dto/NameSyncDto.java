package com.hits.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NameSyncDto {
    private UUID id;

    private FullNameDto fullName;
}
