package com.hits.friends.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="blocking",  uniqueConstraints={
        @UniqueConstraint(columnNames={"main_user", "block_user"}),
})
public class Blocking {

    @Id
    private UUID id;

    private Date dateStart;

    private Date dateEnd;
    @Column(name = "main_user")
    private UUID mainUser;
    @Column(name = "block_user")
    private UUID blockUser;

    private String nameBlocked;
    private String surnameBlocked;
    private String patronymicBlocked;
}
