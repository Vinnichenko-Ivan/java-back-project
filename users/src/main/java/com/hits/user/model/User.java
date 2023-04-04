package com.hits.user.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name="users")
public class User {

    @Id
    private UUID id;

    private Date registrationDate;

    @Column(unique=true)
    private String login;

    @Column(unique=true)
    private String email;

    private String password;

    private String name;
    private String surname;
    private String patronymic;

    private Date birthDate;

    private String phone;

    private String city;

    private UUID avatarId; //for S3

    @PrePersist
    public void generate()
    {
        this.registrationDate = new Date(System.currentTimeMillis());
        this.id = java.util.UUID.randomUUID();
    }

}
