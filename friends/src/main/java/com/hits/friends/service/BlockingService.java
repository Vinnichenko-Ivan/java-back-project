package com.hits.friends.service;

import com.hits.friends.dto.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с блокировками
 */
public interface BlockingService {
    /**
     * Метод добавления блокировки
     * @param addRelationDto
     */
    void addBlock(AddRelationDto addRelationDto);

    /**
     * Метод получения блокировок по фильтру
     * @param queryRelationDto дто для запроса
     * @return Дто набора блокировок
     */
    RelationsDto getBlocking(QueryRelationDto queryRelationDto);

    /**
     * Метод получения блокировки по id
     * @param targetId ид пользователя
     * @return дто блокировки
     */
    FullRelationDto getBlocking(UUID targetId);

    /**
     * Метод отмены блокировки
     * @param targetId ид пользователя
     * @return дто блокировки
     */
    FullRelationDto deleteBlocking(UUID targetId);

    /**
     * Метод поиска. Не реализован.
     */
    void findBlocking();

    /**
     * Проверка есть ли блокировка
     * @param uuid id целевого пользователя
     * @return true - заблокирован false - нет
     */
    Boolean checkBlocking(UUID uuid);
}
