package com.hits.friends.mapper;

import com.hits.common.dto.user.NameSyncDto;
import com.hits.friends.dto.AddRelationDto;
import com.hits.friends.model.Relationship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommonMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nameTarget", source = "fullName.name")
    @Mapping(target = "surnameTarget", source = "fullName.surname")
    @Mapping(target = "patronymicTarget", source = "fullName.patronymic")
    void map(@MappingTarget Relationship relationship, NameSyncDto nameSyncDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nameTarget", source = "fullName.name")
    @Mapping(target = "surnameTarget", source = "fullName.surname")
    @Mapping(target = "patronymicTarget", source = "fullName.patronymic")
    void map(@MappingTarget Relationship relationship,AddRelationDto addRelationDto);
}
