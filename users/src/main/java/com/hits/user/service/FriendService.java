package com.hits.user.service;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.dto.user.NameSyncDto;
import com.hits.user.config.ClientConfiguration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Класс для межсетевых запросов к friends
 */
@FeignClient(name = "friends", url = "http://localhost:8082/friends/common", configuration = ClientConfiguration.class)
public interface FriendService {
    /**
     * синхронизация имени. Происходит при смене профиля.
     * @param nameSyncDto дто
     * @param key ключ апи
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/synchronise", headers = "api-key")
    void nameSynchronization(@RequestBody NameSyncDto nameSyncDto, @RequestHeader("api-key") String key);

    /**
     * Проверка на блокировку для просмотра профиля
     * @param checkDto Дто
     * @param key ключ апи
     * @return True - заблокирован False - не заблокирован
     */
    @RequestMapping(method = RequestMethod.POST, value = "/blocking", headers = "api-key")
    Boolean checkBlocking(@RequestBody CheckDto checkDto, @RequestHeader("api-key") String key);
}
