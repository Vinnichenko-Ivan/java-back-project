package com.hits.friends.service;

import com.hits.friends.dto.*;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с дружбой
 */
public interface FriendshipService {

    /**
     * Добавление друга
     * @param addRelationDto дто дружбы
     */
    void addFriend(AddRelationDto addRelationDto);

    /**
     *
     * @param targetId удаление друга
     * @return Новое дто дружбы
     */
    FullRelationDto deleteFriend(UUID targetId);

    /**
     * Получения списка друзей по филтрам
     * @param queryRelationDto дто запроса с фильтрами
     * @return Список дто с пагинацией
     */
    RelationsDto getFriend(QueryRelationDto queryRelationDto);

    /**
     * Получения друга по его id
     * @param targetId id
     * @return Дто дружбы
     */
    FullRelationDto getFriend(UUID targetId);
}
