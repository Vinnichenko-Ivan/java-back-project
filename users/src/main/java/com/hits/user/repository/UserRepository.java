package com.hits.user.repository;

import com.hits.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository extends CrudRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);

    User getById(UUID id);

    User getByLogin(String login);

    @Query(value =
            "SELECT * FROM users\n" +
            "LIMIT :limit\n" +
            "OFFSET :offset;",
            nativeQuery = true)
    List<User> findAll(int limit, int offset);

    List<User> findAll();

    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
