package com.hits.chat.dto;

import com.hits.common.dto.user.FullNameDto;
import lombok.Data;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class MessageDto {
    private UUID id;
    private Date dateCreated;
    private String text;
    private FullNameDto authorName;
    private UUID authorAvatarId;
    private Set<FileDto> files;
}
