package com.hits.friends.service;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.dto.user.NameSyncDto;
import com.hits.friends.dto.QueryRelationSort;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface CommonService {

    void synchronise(NameSyncDto nameSyncDto);

    void checkUsers(UUID mainId, UUID targetId);

    Boolean checkBlocking(CheckDto checkDto);

    List<Sort.Order> genOrder(QueryRelationSort queryRelationSort);
}
