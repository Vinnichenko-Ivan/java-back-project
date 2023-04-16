package com.hits.user.service;

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
}
