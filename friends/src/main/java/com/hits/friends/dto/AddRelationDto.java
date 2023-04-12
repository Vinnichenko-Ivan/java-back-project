package com.hits.friends.dto;

import com.hits.common.dto.user.FullNameDto;
import lombok.Data;

import java.util.UUID;

@Data
public class AddRelationDto {
    private UUID targetId;

    private FullNameDto fullName;
}
