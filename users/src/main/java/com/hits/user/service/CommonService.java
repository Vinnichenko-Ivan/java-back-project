package com.hits.user.service;

import com.hits.common.dto.user.FullNameDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Сервис для обих функций и межсетевых фунций
 */
public interface CommonService {
    /**
     * Проверка существования пользователя
     * @param id ид
     * @return True - да False - нет
     */
    Boolean checkUser(UUID id);

    FullNameDto getUserName(UUID id);
}
