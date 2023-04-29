package com.hits.friends.repository;

import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Класс репозиторий, нужен для работы с Friendship
 */
public interface FriendsRepository extends CrudRepository<Friendship, UUID>, JpaSpecificationExecutor {

    List<Friendship> getAllByTargetUser(UUID id);

    Friendship getByMainUserAndTargetUser(UUID mainUser, UUID targetUser);

    Boolean existsByMainUserAndTargetUser(UUID mainUser, UUID targetUser);

    List<Friendship> getAllByMainUser(UUID mainUser);

}
