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
@RequestMapping(value = "/blocking", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class BlockingController {

    @PostMapping(value = "/blocking")
    List<FriendDto> getBlocking() {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/blocking")
    FullFriendDto getBlocking(@PathParam("id") UUID uuid) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/blocking/add")
    void addBlocking(@PathParam("id")UUID uuid) {
        throw new NotImplementedException();
    }

    @PatchMapping(value = "/blocking")
    List<FriendDto> synchronise() {
        throw new NotImplementedException();
    }

    @DeleteMapping(value = "/blocking")
    FullFriendDto deleteBlocking(@PathParam("id")UUID uuid) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/blocking/find")
    void findBlocking() {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/blocking/check")
    void checkBlocking() {
        throw new NotImplementedException();
    }
}
