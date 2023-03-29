package com.hits.first.profile.repository;

import com.hits.first.profile.model.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProfileRepository extends CrudRepository<Profile, UUID> {

    Boolean existsByLogin(String login);

    Profile getByLogin(String login);

}
