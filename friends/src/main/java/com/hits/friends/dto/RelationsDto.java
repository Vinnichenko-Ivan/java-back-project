package com.hits.friends.dto;

import com.hits.common.dto.user.PaginationDto;
import lombok.Data;

import java.util.List;

@Data
public class RelationsDto {
    private PaginationDto paginationDto;
    private List<RelationDto> relations;
}
