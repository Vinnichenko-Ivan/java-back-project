package com.hits.chat.mapper;

import com.hits.chat.dto.ChatInfoDto;
import com.hits.chat.model.Chat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatInfoDto map(Chat chat);
}
