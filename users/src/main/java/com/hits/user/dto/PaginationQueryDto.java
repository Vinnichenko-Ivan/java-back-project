package com.hits.user.dto;

import lombok.Data;

@Data
public class PaginationQueryDto {
    private int pageNumber;
    private int size;
}
