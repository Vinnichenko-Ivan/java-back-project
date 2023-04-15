package com.hits.friends.dto;

import com.hits.common.dto.user.FullNameDto;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.UUID;

@Data
public class FullRelationDto {
    private Date dateStart;
    private Date dateEnd;

    private UUID mainUser;
    private UUID targetUser;

    private FullNameDto fullName;
}
