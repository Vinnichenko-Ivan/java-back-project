package com.hits.user.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserFiltersDto {
    private String login;

    private String email;

    private String name;
    private String surname;
    private String patronymic;

    private String phone;

    private String city;

    private UUID avatarId;
}
