package com.hits.user.service;

import com.hits.user.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;

/**
 * Сервис для создания jwt  токенов
 */
public interface JwtService {
    /**
     * Генерация токена для пользователя
     * @param user пользователь
     * @return Токен
     */
    String generateAccessToken(@NonNull User user);

    /**
     * Получения пользователя после авторизации
     * @return пользователь
     */
    User getUser();

}
