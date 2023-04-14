package com.hits.friends.controller;

import com.hits.friends.dto.AddRelationDto;
import com.hits.friends.dto.FullRelationDto;
import com.hits.common.exception.NotImplementedException;
import com.hits.friends.dto.RelationDto;
import com.hits.friends.service.FriendshipService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    List<RelationDto> getFriends() {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/friend")
    FullRelationDto getFriend(@PathParam("id")UUID uuid) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/friend/add")
    void addFriends(AddRelationDto addRelationDto) {
        friendshipService.addFriend(addRelationDto);
    }


    @DeleteMapping(value = "/friend")
    FullRelationDto deleteFriend(@PathParam("id")UUID uuid) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/friend/find")
    void findFriends() {
        throw new NotImplementedException();
    }
}
