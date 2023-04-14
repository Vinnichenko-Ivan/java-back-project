package com.hits.friends.dto;

import com.hits.common.dto.user.FullNameDto;
import lombok.Data;

import java.util.Date;

@Data
public class RelationDto {
    private FullNameDto fullName;
    private Date dateStart;
}
