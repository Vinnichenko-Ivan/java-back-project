package com.hits.friends.dto;

import com.hits.common.dto.user.PaginationQueryDto;
import lombok.Data;

@Data
public class QueryRelationDto {
    private PaginationQueryDto paginationQueryDto;
    private QueryRelationFilter queryRelationFilter;
    private QueryRelationSort queryRelationSort;
}
