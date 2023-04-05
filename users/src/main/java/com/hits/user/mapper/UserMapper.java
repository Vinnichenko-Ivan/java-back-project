package com.hits.user.mapper;


import com.hits.user.dto.UserDto;
import com.hits.user.dto.UserEditDto;
import com.hits.user.model.User;
import com.hits.user.dto.UserRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User map(UserRegisterDto userRegisterDto);
    void map(@MappingTarget User user, UserEditDto userEditDto);

    UserDto map(User user);
}
