package com.hits.chat.dto;

import com.hits.common.dto.user.FullNameDto;
import com.hits.common.dto.user.PaginationQueryDto;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ChatFindInfoDto {
    private UUID id;
    private String name;
    private String lastMessageText;
    private Date lastMessageDate;
    private FullNameDto lastMessageAuthor;
}
