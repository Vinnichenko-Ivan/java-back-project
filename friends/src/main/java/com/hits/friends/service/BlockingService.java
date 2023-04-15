package com.hits.friends.service;

import com.hits.friends.dto.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

public interface BlockingService {
    void addBlock(AddRelationDto addRelationDto);

    RelationsDto getBlocking(QueryRelationDto queryRelationDto);

    FullRelationDto getBlocking(UUID targetId);

    FullRelationDto deleteBlocking(UUID targetId);

    void findBlocking();

    Boolean checkBlocking(UUID uuid);
}
