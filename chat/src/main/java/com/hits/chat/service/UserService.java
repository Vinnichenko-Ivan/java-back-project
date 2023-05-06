package com.hits.chat.service;

import com.hits.chat.config.ClientConfiguration;
import com.hits.common.dto.user.FullNameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * Сервис отправляющий межсервисные запросы user.
 */
@FeignClient(name = "user", url = "http://localhost:8081/users/common", configuration = ClientConfiguration.class)
public interface UserService {
    /**
     * Проверка существует ли такой пользователь
     * @param id ид пользователя
     * @param key ключ апи
     * @return True - да False - нет
     */
    @RequestMapping(method = RequestMethod.GET, value = "/check/{id}", headers = "api-key")
    Boolean checkUser(@PathVariable UUID id, @RequestHeader("api-key") String key);

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}", headers = "api-key")
    FullNameDto getUserName(@PathVariable UUID id, @RequestHeader("api-key") String key);
}
