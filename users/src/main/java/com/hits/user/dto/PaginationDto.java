package com.hits.user.dto;

import lombok.Data;

@Data
public class PaginationDto {
    private int pageNumber;
    private int maxPage;
    private int size;
}
