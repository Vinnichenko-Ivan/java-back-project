package com.hits.friends.repository;

import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FriendsRepository extends CrudRepository<Friendship, UUID> {

    List<Friendship> getAllByTargetUser(UUID id);

    Friendship getByMainUserAndTargetUser(UUID mainUser, UUID targetUser);

    Boolean existsByMainUserAndTargetUser(UUID mainUser, UUID targetUser);

    List<Friendship> getAllByMainUser(UUID mainUser);

    @Query(nativeQuery = true,
            value = "SELECT * FROM friendship\n" +
                    "WHERE\n" +
                    "LOWER(name_target) LIKE '%'|| :name_target ||'%' AND\n" +
                    "LOWER(surname_target) LIKE '%'|| :surname_target ||'%' AND\n" +
                    "LOWER(patronymic_target) LIKE '%'|| :patronymic_target ||'%' AND\n" +
                    "main_user = :main_user AND" +
                    "date_end IS NULL",
            countQuery = "SELECT count(*) FROM friendship\n" +
                    "WHERE\n" +
                    "LOWER(name_target) LIKE '%'|| :name_target ||'%' AND\n" +
                    "LOWER(surname_target) LIKE '%'|| :surname_target ||'%' AND\n" +
                    "LOWER(patronymic_target) LIKE '%'|| :patronymic_target ||'%' AND\n" +
                    "main_user = :main_user AND \n" +
                    "date_end IS NULL \n")
    Page<Friendship> getAllByFilter(@Param("name_target") String name,
                                  @Param("surname_target") String surname,
                                  @Param("patronymic_target") String patronymic,
//                                  @Param("date_first") Date date_first,
//                                  @Param("date_second") Date date_second,
                                  @Param("main_user") UUID mainId,
                                  Pageable pageable);
}
