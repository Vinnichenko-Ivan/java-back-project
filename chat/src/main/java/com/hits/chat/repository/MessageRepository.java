package com.hits.chat.repository;

import com.hits.chat.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<Message, UUID> {
    List<Message> getAllByChat_Id(UUID id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM message as m WHERE\n" +
                    "    m.chat_id IN (SELECT distinct(chat_id) FROM chat_users WHERE users = ?1)\n" +
                    "    AND (LOWER(m.text) LIKE '%' || ?2 || '%'\n" +
                    "        OR ((SELECT count(*) FROM message_files\n" +
                    "             WHERE message_id = m.id\n" +
                    "               AND file_name LIKE '%' || ?2 || '%')\n" +
                    "                > 0\n" +
                    "             )) ORDER BY m.created_date")
    List<Message> getMessages(UUID userId, String find);
}
