package com.hits.friends.repository;

import com.hits.friends.model.Blocking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;


import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Класс репозиторий, нужен для работы с Blocking
 */
public interface BlockingRepository extends CrudRepository<Blocking, UUID>, JpaSpecificationExecutor {

    List<Blocking> getAllByTargetUser(UUID id);

    Blocking getByMainUserAndTargetUser(UUID mainUser, UUID targetUser);

    Boolean existsByMainUserAndTargetUser(UUID mainUser, UUID targetUser);

    List<Blocking> getAllByMainUser(UUID mainUser);

}
