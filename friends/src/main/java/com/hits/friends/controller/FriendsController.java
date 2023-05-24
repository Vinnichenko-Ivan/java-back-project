package com.hits.friends.controller;

import com.hits.common.service.Utils;
import com.hits.friends.dto.*;
import com.hits.common.exception.NotImplementedException;
import com.hits.friends.service.FriendshipService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

import static com.hits.common.Paths.*;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class FriendsController {

    private final FriendshipService friendshipService;
    @PostMapping(value = FRIEND_FRIENDSHIP_GET_FRIENDS)
    RelationsDto getFriends(@Valid  @RequestBody QueryRelationDto queryRelationDto) {
        Utils.logQuery(FRIEND_FRIENDSHIP_GET_FRIENDS, queryRelationDto);
        return friendshipService.getFriend(queryRelationDto);
    }

    @GetMapping(value = FRIEND_FRIENDSHIP_GET_FRIEND)
    FullRelationDto getFriend(@PathVariable UUID id) {
        Utils.logQuery(FRIEND_FRIENDSHIP_GET_FRIEND, id);
        return friendshipService.getFriend(id);
    }

    @PostMapping(value = FRIEND_FRIENDSHIP_ADD)
    void addFriends(@Valid @RequestBody AddRelationDto addRelationDto) {
        Utils.logQuery(FRIEND_FRIENDSHIP_ADD, addRelationDto);
        friendshipService.addFriend(addRelationDto);
    }


    @DeleteMapping(value = FRIEND_FRIENDSHIP_DELETE)
    FullRelationDto deleteFriend(@PathVariable UUID id) {
        Utils.logQuery(FRIEND_FRIENDSHIP_DELETE, id);
        return friendshipService.deleteFriend(id);
    }


    @Deprecated
    @PostMapping(value = FRIEND_FRIENDSHIP_FIND)
    void findFriends() {
        Utils.logQuery(FRIEND_FRIENDSHIP_FIND);
        throw new NotImplementedException();
    }
}
