package com.hits.friends.mapper;

import com.hits.friends.dto.NameSyncDto;
import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {
    @Mapping(target = "nameFriend", source = "fullName.name")
    @Mapping(target = "surnameFriend", source = "fullName.surname")
    @Mapping(target = "patronymicFriend", source = "fullName.patronymic")
    void map(@MappingTarget Friendship friendship, NameSyncDto nameSyncDto);
}
