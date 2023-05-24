package com.hits.common.dto.user;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PaginationDto {
    private int pageNumber;
    private int maxPage;
    private int size;
}
