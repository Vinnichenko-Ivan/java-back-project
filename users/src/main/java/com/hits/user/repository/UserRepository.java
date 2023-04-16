package com.hits.user.repository;

import com.hits.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository extends CrudRepository<User, UUID> {

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);

    User getById(UUID id);

    User getByLogin(String login);

    @Query(value =
            "SELECT * FROM users\n" +
            "LIMIT :limit\n" +
            "OFFSET :offset;",
            nativeQuery = true)
    List<User> findAll(int limit, int offset);

    /**
     * Метод получения пользователей по фильтрам
     * @param login логин
     * @param email почта
     * @param name имя
     * @param surname фамилия
     * @param patronymic отчество
     * @param phone телефон
     * @param city город
     * @param pageable пагинация и сортировка
     * @return Page<User>
     */
    @Query(value = "SELECT * FROM users\n" +
            "WHERE\n" +
            "    LOWER(login) LIKE '%'|| :login ||'%' AND\n" +
            "    LOWER(email) LIKE '%'|| :email ||'%' AND\n" +
            "    LOWER(name) LIKE '%'|| :name ||'%' AND\n" +
            "    LOWER(surname) LIKE '%'|| :surname ||'%' AND\n" +
            "    LOWER(patronymic) LIKE '%'|| :patronymic ||'%' AND\n" +
            "    LOWER(phone) LIKE '%'|| :phone ||'%' AND\n" +
            "    LOWER(city) LIKE '%'|| :city ||'%'",
    countQuery = "SELECT count(*) FROM users\n" +
            "WHERE\n" +
            "    LOWER(login) LIKE '%'|| :login ||'%' AND\n" +
            "    LOWER(email) LIKE '%'|| :email ||'%' AND\n" +
            "    LOWER(name) LIKE '%'|| :name ||'%' AND\n" +
            "    LOWER(surname) LIKE '%'|| :surname ||'%' AND\n" +
            "    LOWER(patronymic) LIKE '%'|| :patronymic ||'%' AND\n" +
            "    LOWER(phone) LIKE '%'|| :phone ||'%' AND\n" +
            "    LOWER(city) LIKE '%'|| :city ||'%'",
    nativeQuery = true)
    Page<User> getAllByFilter(@Param("login") String login,
                              @Param("email") String email,
                              @Param("name") String name,
                              @Param("surname") String surname,
                              @Param("patronymic") String patronymic,
                              @Param("phone") String phone,
                              @Param("city") String city,
                              Pageable pageable);
    List<User> findAll();
}
