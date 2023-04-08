package com.hits.friends.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="friendship",  uniqueConstraints={
        @UniqueConstraint(columnNames={"main_user", "friend_user"}),
})
public class Friendship {

    @Id
    private UUID id;

    private Date dateStart;

    private Date dateEnd;

    @Column(name = "main_user")
    private UUID mainUser;
    @Column(name = "friend_user")
    private UUID friendUser;

    private String nameFriend;
    private String surnameFriend;
    private String patronymicFriend;
}
