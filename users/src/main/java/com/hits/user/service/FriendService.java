package com.hits.user.service;

import com.hits.user.config.ClientConfiguration;
import com.hits.user.dto.NameSyncDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "friends", url = "http://localhost:8082", configuration = ClientConfiguration.class)
public interface FriendService {

    @RequestMapping(method = RequestMethod.PATCH, value = "/synchronise")
    void nameSynchronization(NameSyncDto nameSyncDto);
}
