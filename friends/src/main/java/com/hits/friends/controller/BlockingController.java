package com.hits.friends.controller;

import com.hits.friends.dto.AddRelationDto;

import com.hits.friends.dto.FullRelationDto;
import com.hits.common.exception.NotImplementedException;
import com.hits.friends.dto.RelationDto;
import com.hits.friends.service.BlockingService;
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
@RequestMapping(value = "/friends/blocking", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class BlockingController {

    private final BlockingService blockingService;

    @PostMapping(value = "/blocking")
    List<RelationDto> getBlocking() {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/blocking")
    FullRelationDto getBlocking(@PathParam("id") UUID uuid) {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/blocking/add")
    void addBlocking(AddRelationDto addRelationDto) {
        blockingService.addBlock(addRelationDto);
    }



    @DeleteMapping(value = "/blocking")
    FullRelationDto deleteBlocking(@PathParam("id")UUID uuid) {
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
