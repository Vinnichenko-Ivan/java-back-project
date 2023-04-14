package com.hits.friends.service;

import com.hits.friends.config.ClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user", url = "http://localhost:8082/friends/common", configuration = ClientConfiguration.class)
public interface UserService {

}
