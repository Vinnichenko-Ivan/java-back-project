package com.hits.friends.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class NewRelationDto {
    private UUID targetUser;

    private FullNameDto fullNameDto;
}
