package com.hits.chat.mapper;

import com.hits.chat.dto.MessageDto;
import com.hits.chat.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "dateCreated", source = "createdDate")
    MessageDto map(Message message);

    List<MessageDto> map(List<Message> message);

}
