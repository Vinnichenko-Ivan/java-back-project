package com.hits.user.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.UUID;

import static com.hits.user.config.RegexConfig.*;

@Data
public class UserEditDto {
    @NotNull
    @Pattern(regexp = LOGIN)
    private String login;

    @NotNull
    @Pattern(regexp = EMAIL)
    private String email;

    @NotNull
    @Pattern(regexp = PASSWORD)
    private String password;

    private String name;
    private String surname;
    private String patronymic;

    private Date birthDate;

    @NotNull
    @Pattern(regexp = PHONE)
    private String phone;

    private String city;

    private UUID avatarId;
}