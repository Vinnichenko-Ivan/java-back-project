package com.hits.first.profile.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;

@Data
public class ProfileRegisterDto {
    @NotEmpty(message = "login.empty")
    private String login;
    private String name;
    private String surname;
    private String patronymic;
    private Date birthDay;
    @NotEmpty(message = "password.empty")
    private String password;
}
