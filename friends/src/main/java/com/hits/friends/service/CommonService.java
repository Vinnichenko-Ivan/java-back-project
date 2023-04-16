package com.hits.friends.service;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.dto.user.NameSyncDto;
import com.hits.friends.dto.QueryRelationSort;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;
/**
 * Сервис для общих функций и межсервисных функций
 */
public interface CommonService {
    /**
     * Синхронизация имени пользователя. Происходит при смене фио аккаунта
     * @param nameSyncDto дто для смены
     */
    void synchronise(NameSyncDto nameSyncDto);

    /**
     * Проверяет существут ли два пользователя(Метод для внутреннего пользователя)
     * @param mainId ид
     * @param targetId ид
     */
    void checkUsers(UUID mainId, UUID targetId);

    /**
     * Проверка блокировки
     * @param checkDto дто
     * @return True - да False - нет
     */
    Boolean checkBlocking(CheckDto checkDto);

    /**
     * Генератор параметров для сортировки(Для внутреннего пользования)
     * @param queryRelationSort - дто параметров
     * @return список List<Sort.Order>
     */
    List<Sort.Order> genOrder(QueryRelationSort queryRelationSort);
}
