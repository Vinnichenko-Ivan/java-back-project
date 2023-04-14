package com.hits.friends.service;

import com.hits.friends.dto.AddRelationDto;

import com.hits.friends.dto.FullRelationDto;
import com.hits.friends.dto.RelationDto;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

public interface BlockingService {
    void addBlock(AddRelationDto addRelationDto);

    List<RelationDto> getBlocking();

    FullRelationDto getBlocking(@PathParam("id") UUID uuid);

    FullRelationDto deleteBlocking(@PathParam("id")UUID uuid);

    void findBlocking();

    void checkBlocking();
}
