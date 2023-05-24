package com.hits.friends.controller;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.service.Utils;
import com.hits.friends.dto.*;

import com.hits.common.exception.NotImplementedException;
import com.hits.friends.service.BlockingService;
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
public class BlockingController {

    private final BlockingService blockingService;

    @PostMapping(value = FRIEND_BLOCKING_GET_BLOCKING)
    RelationsDto getBlocking(@Valid @RequestBody QueryRelationDto queryRelationDto) {
        Utils.logQuery(FRIEND_BLOCKING_GET_BLOCKING, queryRelationDto);
        return blockingService.getBlocking(queryRelationDto);
    }

    @GetMapping(value = FRIEND_BLOCKING_GET_BLOCK)
    FullRelationDto getBlocking(@PathVariable("id") UUID uuid) {
        Utils.logQuery(FRIEND_BLOCKING_GET_BLOCK, uuid);
        return blockingService.getBlocking(uuid);
    }

    @PostMapping(value = FRIEND_BLOCKING_ADD)
    void addBlocking(@Valid @RequestBody AddRelationDto addRelationDto) {
        Utils.logQuery(FRIEND_BLOCKING_ADD, addRelationDto);
        blockingService.addBlock(addRelationDto);
    }

    @DeleteMapping(value = FRIEND_BLOCKING_DELETE)
    FullRelationDto deleteBlocking(@PathVariable("id")UUID uuid) {
        Utils.logQuery(FRIEND_BLOCKING_DELETE, uuid);
        return blockingService.deleteBlocking(uuid);
    }

    @PostMapping(value = FRIEND_BLOCKING_FIND)
    void findBlocking() {
        Utils.logQuery(FRIEND_BLOCKING_FIND);
        throw new NotImplementedException();
    }

    @GetMapping(value = FRIEND_BLOCKING_CHECK)
    Boolean checkBlocking(@PathVariable UUID id) {
        Utils.logQuery(FRIEND_BLOCKING_CHECK, id);
        return blockingService.checkBlocking(id);
    }
}
