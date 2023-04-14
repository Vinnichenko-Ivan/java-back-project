package com.hits.user.dto;

import com.hits.common.dto.user.PaginationDto;
import lombok.Data;

import java.util.List;

@Data
public class UsersDto {

    private List<UserDto> users;
    private PaginationDto paginationDto;

}
