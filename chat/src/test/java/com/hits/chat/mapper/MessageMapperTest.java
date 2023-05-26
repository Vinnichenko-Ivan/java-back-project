package com.hits.chat.mapper;

import com.hits.chat.dto.MessageDto;
import com.hits.chat.model.Chat;
import com.hits.chat.model.Message;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MessageMapperTest {
    private final MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);

    @Test
    void mapTest() {
        Message message = new Message();
        message.setFiles(null);
        message.setText("text");
        message.setChat(new Chat());
        message.setId(UUID.randomUUID());
        message.setCreatedDate(new Date(10101));
        message.setAuthorId(UUID.randomUUID());
        MessageDto dto = messageMapper.map(message);
        assertEquals(message.getText(), dto.getText());
        assertNull(dto.getAuthorName());
        assertEquals(message.getId(), dto.getId());
        assertEquals(message.getCreatedDate(), dto.getDateCreated());
    }
    @Test
    void mapListTest() {
        Message message = new Message();
        message.setFiles(null);
        message.setText("text");
        message.setChat(new Chat());
        message.setId(UUID.randomUUID());
        message.setCreatedDate(new Date(10101));
        message.setAuthorId(UUID.randomUUID());
        List<MessageDto> dto = messageMapper.map(List.of(message));
        assertEquals(message.getText(), dto.get(0).getText());
        assertNull(dto.get(0).getAuthorName());
        assertEquals(message.getId(), dto.get(0).getId());
        assertEquals(message.getCreatedDate(), dto.get(0).getDateCreated());
    }
}
