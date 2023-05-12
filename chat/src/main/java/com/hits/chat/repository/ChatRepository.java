package com.hits.chat.repository;

import com.hits.chat.model.Chat;
import com.hits.chat.model.ChatType;
import com.hits.chat.model.Message;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ChatRepository extends CrudRepository<Chat, UUID>, JpaSpecificationExecutor<Chat> {

    @Query(nativeQuery = true,
    value = "SELECT * FROM (\n" +
            "                  SELECT * FROM chat\n" +
            "                  WHERE chat.chat_type = 'PRIVATE'\n" +
            "              ) chat JOIN chat_users ON chat.id = chat_users.chat_id\n" +
            "WHERE users = ?1 OR users = ?2 LIMIT 1\n")
    Chat getPrivateChat(UUID firstId, UUID secondId);


}
