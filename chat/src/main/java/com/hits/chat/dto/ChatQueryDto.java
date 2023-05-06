package com.hits.chat.dto;

import com.hits.common.dto.user.PaginationQueryDto;
import lombok.Data;

@Data
public class ChatQueryDto {
    private PaginationQueryDto paginationQueryDto;
    private String nameFilter;
}
