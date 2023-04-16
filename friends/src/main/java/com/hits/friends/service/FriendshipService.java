package com.hits.friends.service;

import com.hits.friends.dto.*;

import java.util.List;
import java.util.UUID;

public interface FriendshipService {

    void addFriend(AddRelationDto addRelationDto);

    FullRelationDto deleteFriend(UUID targetId);

    RelationsDto getFriend(QueryRelationDto queryRelationDto);

    FullRelationDto getFriend(UUID targetId);
}
