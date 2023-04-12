package com.hits.friends.repository;

import com.hits.friends.model.Blocking;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BlockingRepository extends CrudRepository<Blocking, UUID> {

    List<Blocking> getAllByTargetUser(UUID id);
}
