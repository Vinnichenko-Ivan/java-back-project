package com.hits.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserEditDto {
    private String login;

    private String email;

    private String password;

    private String name;
    private String surname;
    private String patronymic;

    private Date birthDate;

    private String phone;

    private String city;

    private UUID avatarId;
}
