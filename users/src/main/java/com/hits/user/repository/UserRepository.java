package com.hits.user.repository;

import com.hits.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Boolean existsByLogin(String login);

    Boolean existsByPhone(String phone);

    User getByLogin(String login);

}