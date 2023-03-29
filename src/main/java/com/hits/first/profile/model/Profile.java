package com.hits.first.profile.model;

import lombok.Data;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class Profile {

    @Id
    private UUID id;

    @Column(unique=true)
    @NotNull
    private String login;

    private String name;

    private String surname;

    private String patronymic;

    private Date birthDay;

    private Date registerDate;

    private String password;

    @PrePersist
    public void generate()
    {
        this.id = java.util.UUID.randomUUID();
    }

}
