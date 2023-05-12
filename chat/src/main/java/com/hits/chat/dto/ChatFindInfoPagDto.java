package com.hits.chat.dto;

import com.hits.common.dto.user.PaginationDto;
import lombok.Data;

import java.util.List;

@Data
public class ChatFindInfoPagDto {
    private List<ChatFindInfoDto> chatFindInfoDtos;
    private PaginationDto paginationDto;
}
