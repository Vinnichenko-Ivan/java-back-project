package com.hits.common.dto.user;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PaginationQueryDto {
    @Min(1) //TODO не работает. Починить
    private int pageNumber;
    @Min(1)
    private int size;
}
