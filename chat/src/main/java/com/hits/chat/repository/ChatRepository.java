package com.hits.chat.repository;

import com.hits.chat.model.Chat;
import com.hits.chat.model.ChatType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ChatRepository extends CrudRepository<Chat, UUID> {

    Chat getChatByChatTypeAndUsers(ChatType chatType, Set<UUID> users);
}
