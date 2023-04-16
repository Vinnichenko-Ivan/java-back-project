package com.hits.user.service;

/**
 * Сервис для работы с паролями. Пока для дебага не хэшируем пароль.
 */
public interface PasswordService {
    /**
     * Перевод пароля в хэш
     * @param password пароль
     * @return хэш пароля. Пока что для дебага сам пароль.
     */
    String toHash(String password);

    /**
     * Сравнение пароля и хэша
     * @param password пароль
     * @param hash хэш
     * @return пока для дебага просто сравниваем строки
     */
    Boolean comparison(String password, String hash);
}
