package com.hits.chat.dto;

import com.hits.common.dto.user.PaginationQueryDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChatQueryDto {
    @NotNull
    private PaginationQueryDto paginationQueryDto;
    private String nameFilter;
}
