package com.hits.friends.controller;

import com.hits.friends.dto.FriendDto;
import com.hits.friends.dto.FullFriendDto;
import com.hits.friends.exception.NotImplementedException;
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
@RequestMapping(value = "/friends", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class FriendsController {

    @PostMapping(value = "/friends")
    List<FriendDto> getFriends() {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/friend")
    FullFriendDto getFriend(@PathParam("id")UUID uuid) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/friend/add")
    void addFriends(@PathParam("id")UUID uuid) {
        throw new NotImplementedException();
    }

    @PatchMapping(value = "/friends")
    List<FriendDto> synchronise() {
        throw new NotImplementedException();
    }

    @DeleteMapping(value = "/friend")
    FullFriendDto deleteFriend(@PathParam("id")UUID uuid) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/friend/find")
    void findFriends() {
        throw new NotImplementedException();
    }
}
