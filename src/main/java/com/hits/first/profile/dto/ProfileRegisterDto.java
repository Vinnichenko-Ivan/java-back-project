package com.hits.first.profile.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileRegisterDto {
    private String login;
    private String name;
    private String surname;
    private String patronymic;
    private Date birthDay;
    private Date registerDate;
    private String password;
}
