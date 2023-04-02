package com.hits.user.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String login;
    private String name;
    private String surname;
    private String patronymic;
    private Date birthDay;
    private Date registerDate;
    private String password;
}
