package com.hits.user.dto;

import com.hits.common.dto.user.PaginationQueryDto;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class UsersQueryDto {
    private PaginationQueryDto paginationQueryDto;
    private UserFiltersDto userFiltersDto;
    private UserSortFieldDto userSortFieldDto;
}
