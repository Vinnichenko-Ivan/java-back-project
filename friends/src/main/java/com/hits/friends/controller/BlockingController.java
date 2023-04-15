package com.hits.friends.controller;

import com.hits.common.dto.user.CheckDto;
import com.hits.friends.dto.*;

import com.hits.common.exception.NotImplementedException;
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
    RelationsDto getBlocking(QueryRelationDto queryRelationDto) {
        return blockingService.getBlocking(queryRelationDto);
    }

    @GetMapping(value = "/blocking/{id}")
    FullRelationDto getBlocking(@PathVariable("id") UUID uuid) {
        return blockingService.getBlocking(uuid);
    }

    @PostMapping(value = "/blocking/add")
    void addBlocking(AddRelationDto addRelationDto) {
        blockingService.addBlock(addRelationDto);
    }

    @DeleteMapping(value = "/blocking/{id}")
    FullRelationDto deleteBlocking(@PathVariable("id")UUID uuid) {
        return blockingService.deleteBlocking(uuid);
    }

    @PostMapping(value = "/blocking/find")
    void findBlocking() {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/blocking/check/{id}")
    Boolean checkBlocking(@PathVariable UUID id) {
        return blockingService.checkBlocking(id);
    }
}
