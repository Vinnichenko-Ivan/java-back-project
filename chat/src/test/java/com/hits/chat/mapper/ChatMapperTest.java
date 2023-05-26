package com.hits.chat.mapper;

import com.hits.chat.dto.ChatInfoDto;
import com.hits.chat.model.Chat;
import com.hits.chat.model.ChatType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatMapperTest {
    private final ChatMapper chatMapper = Mappers.getMapper(ChatMapper.class);

    @Test
    void mapTest() {
        Chat chat = new Chat();
        chat.setChatType(ChatType.PRIVATE);
        chat.setAvatarId(UUID.randomUUID());
        chat.setUsers(Set.of(UUID.randomUUID(), UUID.randomUUID()));
        chat.setName("name");
        chat.setAdminUser(UUID.randomUUID());
        chat.setCreatedDate(new Date(10101));
        chat.setId(UUID.randomUUID());
        chat.setMessages(null);
        chat.setLastMessageId(UUID.randomUUID());

        ChatInfoDto dto = chatMapper.map(chat);

        assertEquals(chat.getName(), dto.getName());
        assertEquals(chat.getAdminUser(), dto.getAdminId());
        assertEquals(chat.getAvatarId(), dto.getAvatarId());
        assertEquals(chat.getCreatedDate(), dto.getDateCreated());
    }
}
