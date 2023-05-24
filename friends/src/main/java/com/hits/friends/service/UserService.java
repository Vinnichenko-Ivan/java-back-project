package com.hits.friends.service;

import com.hits.common.dto.user.FullNameDto;
import com.hits.friends.config.ClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

import static com.hits.common.Paths.*;

/**
 * Сервис отправляющий межсервисные запросы user.
 */
@FeignClient(name = USER_SERVICE_NAME, url = USER_SERVICE_PATH, configuration = ClientConfiguration.class)
public interface UserService {
    /**
     * Проверка существует ли такой пользователь
     * @param id ид пользователя
     * @param key ключ апи
     * @return True - да False - нет
     */
    @RequestMapping(method = RequestMethod.GET, value = USER_CHECK_USER, headers = API_SECURE_HEADER)
    Boolean checkUser(@PathVariable UUID id, @RequestHeader(API_SECURE_HEADER) String key);

    @RequestMapping(method = RequestMethod.GET, value = USER_USER_NAME, headers = API_SECURE_HEADER)
    FullNameDto getUserName(@PathVariable UUID id, @RequestHeader(API_SECURE_HEADER) String key);
}
