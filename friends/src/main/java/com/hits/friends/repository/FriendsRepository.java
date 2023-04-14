package com.hits.friends.repository;

import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface FriendsRepository extends CrudRepository<Friendship, UUID> {

    List<Friendship> getAllByTargetUser(UUID id);

    Friendship getByMainUserAndTargetUser(UUID mainUser, UUID targetUser);

    Boolean existsByMainUserAndTargetUser(UUID mainUser, UUID targetUser);

    List<Friendship> getAllByMainUser(UUID mainUser);
}
