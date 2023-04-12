package com.hits.user.repository;

import com.hits.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);

    User getByLogin(String login);

    @Query(value =
            "SELECT * FROM users\n" +
            "LIMIT :limit\n" +
            "OFFSET :offset;",
            nativeQuery = true)
    List<User> findAll(int limit, int offset);

    List<User> findAll();
}
