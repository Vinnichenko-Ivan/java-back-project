package com.hits.common.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class NameSyncDto {

    private UUID id;
    private FullNameDto fullName;
}
