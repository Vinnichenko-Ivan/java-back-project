package com.hits.chat.mapper;

import com.hits.chat.dto.MessageDto;
import com.hits.chat.model.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto map(Message message);

    List<MessageDto> map(List<Message> message);
}
