package com.hits.first.profile.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class Profile {

    @Id
    private UUID id;

    @Column(unique=true)
    private String login;

    private String name;

    private String surname;

    private String patronymic;

    private Date birthDay;

    private Date registerDate;

    private String password;

}
