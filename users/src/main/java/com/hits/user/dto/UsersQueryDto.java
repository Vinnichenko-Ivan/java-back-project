package com.hits.user.dto;

import lombok.Data;

@Data
public class UsersQueryDto {

    private PaginationQueryDto paginationQueryDto;
    private UserFiltersDto userFiltersDto;
    private UserSortFieldDto userSortFieldDto;

}
