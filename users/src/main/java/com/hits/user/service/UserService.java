package com.hits.user.service;

import com.hits.user.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Сервис для работы с пользователями
 */
public interface UserService {
    /**
     * Регистрация
     * @param userRegisterDto дто регистрации
     * @return дто зарегестрированного пользователя
     */
    UserDto register(UserRegisterDto userRegisterDto);

    /**
     * Авторизация
     * @param credentialsDto дто
     * @return Дто пользователя + Хэдер токена
     */
    ResponseEntity<UserDto> authorize(CredentialsDto credentialsDto);

    /**
     * Метод измения профиля пользователя
     * @param userEditDto дто изменения
     * @return Дто измененного пользователя
     */
    UserDto putUser(UserEditDto userEditDto);

    /**
     * Получения себя
     * @return Дто профиля себя
     */
    UserDto getMe();

    /**
     * Получения профиля по логину с учетом блокировки
     * @param login логин
     * @return дто пользоватеяля
     */
    UserDto getUser(String login);

    /**
     * Получение списка пользователей по фильтрам
     * @param usersQueryDto дто с фильтрами
     * @return Список пользователей
     */
    UsersDto getUsers(UsersQueryDto usersQueryDto);
}

