package com.hits.friends.mapper;

import com.hits.friends.dto.NameSyncDto;
import com.hits.friends.model.Friendship;
import com.hits.friends.model.Relationship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommonMapper {
    @Mapping(target = "nameTarget", source = "fullName.name")
    @Mapping(target = "surnameTarget", source = "fullName.surname")
    @Mapping(target = "patronymicTarget", source = "fullName.patronymic")
    void map(@MappingTarget Relationship relationship, NameSyncDto nameSyncDto);
}
