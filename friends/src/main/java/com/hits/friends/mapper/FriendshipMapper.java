package com.hits.friends.mapper;

import com.hits.friends.dto.FullRelationDto;
import com.hits.friends.dto.RelationDto;
import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {

    @Mapping(target = "fullName.name", source = "nameTarget")
    @Mapping(target = "fullName.surname", source = "surnameTarget")
    @Mapping(target = "fullName.patronymic", source = "patronymicTarget")
    RelationDto map(Friendship friendship);

    @Mapping(target = "fullName.name", source = "nameTarget")
    @Mapping(target = "fullName.surname", source = "surnameTarget")
    @Mapping(target = "fullName.patronymic", source = "patronymicTarget")
    FullRelationDto mapToFull(Friendship friendship);
}
