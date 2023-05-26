package com.hits.chat.mapper;

import com.hits.chat.dto.ChatInfoDto;
import com.hits.chat.model.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(target = "adminId", source = "adminUser")
    @Mapping(target = "dateCreated", source = "createdDate")
    ChatInfoDto map(Chat chat);
}
