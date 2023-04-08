package com.hits.friends.mapper;

import com.hits.friends.dto.NameSyncDto;
import com.hits.friends.model.Blocking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BlockingMapper {

    @Mapping(target = "nameBlocked", source = "fullName.name")
    @Mapping(target = "surnameBlocked", source = "fullName.surname")
    @Mapping(target = "patronymicBlocked", source = "fullName.patronymic")
    void map(@MappingTarget Blocking blocking, NameSyncDto nameSyncDto);
}
