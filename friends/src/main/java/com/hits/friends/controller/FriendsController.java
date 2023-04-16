package com.hits.friends.controller;

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

@Api
@RestController
@RequestMapping(value = "/friends/friends", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class FriendsController {

    private final FriendshipService friendshipService;
    @PostMapping(value = "/friends")
    RelationsDto getFriends(@Valid  @RequestBody QueryRelationDto queryRelationDto) {
        return friendshipService.getFriend(queryRelationDto);
    }

    @GetMapping(value = "/friend/{id}")
    FullRelationDto getFriend(@PathVariable UUID id) {
        return friendshipService.getFriend(id);
    }

    @PostMapping(value = "/friend/add")
    void addFriends(@Valid @RequestBody AddRelationDto addRelationDto) {
        friendshipService.addFriend(addRelationDto);
    }


    @DeleteMapping(value = "/friend/{id}")
    FullRelationDto deleteFriend(@PathVariable UUID id) {
        return friendshipService.deleteFriend(id);
    }

    @PostMapping(value = "/friend/find")
    void findFriends() {
        throw new NotImplementedException();
    }
}
