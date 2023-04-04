package com.hits.user.mapper;


import com.hits.user.dto.UserDto;
import com.hits.user.dto.UserEditDto;
import com.hits.user.model.User;
import com.hits.user.dto.UserRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User map(UserRegisterDto userRegisterDto);
    void map(@MappingTarget User user, UserEditDto userEditDto);

    UserDto map(User user);
}
