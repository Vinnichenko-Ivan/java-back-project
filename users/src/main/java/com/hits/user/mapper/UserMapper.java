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

    @Mapping(target = "name", source = "fullName.name")
    @Mapping(target = "surname", source = "fullName.surname")
    @Mapping(target = "patronymic", source = "fullName.patronymic")
    @Mapping(target = "password", ignore = true)
    User map(UserRegisterDto userRegisterDto);

    @Mapping(target = "name", source = "fullName.name")
    @Mapping(target = "surname", source = "fullName.surname")
    @Mapping(target = "patronymic", source = "fullName.patronymic")
    void map(@MappingTarget User user, UserEditDto userEditDto);

    @Mapping(target = "fullName.name", source = "name")
    @Mapping(target = "fullName.surname", source = "surname")
    @Mapping(target = "fullName.patronymic", source = "patronymic")
    UserDto map(User user);
}
