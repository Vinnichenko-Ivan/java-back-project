package com.hits.user.mapper;


import com.hits.common.dto.user.FullNameDto;
import com.hits.common.dto.user.NameSyncDto;
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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    User map(UserRegisterDto userRegisterDto);

    @Mapping(target = "name", source = "fullName.name")
    @Mapping(target = "surname", source = "fullName.surname")
    @Mapping(target = "patronymic", source = "fullName.patronymic")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    void map(@MappingTarget User user, UserEditDto userEditDto);

    @Mapping(target = "fullName.name", source = "name")
    @Mapping(target = "fullName.surname", source = "surname")
    @Mapping(target = "fullName.patronymic", source = "patronymic")
    UserDto map(User user);

    @Mapping(target = "fullName.name", source = "name")
    @Mapping(target = "fullName.surname", source = "surname")
    @Mapping(target = "fullName.patronymic", source = "patronymic")
    NameSyncDto mapToSync(User user);

    FullNameDto mapToName(User user);
}
