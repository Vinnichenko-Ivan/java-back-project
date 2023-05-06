package com.hits.chat.repository;

import com.hits.chat.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<Message, UUID> {
    List<Message> getAllByChat_Id(UUID id);
}
