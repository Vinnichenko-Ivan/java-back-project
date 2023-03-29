package com.hits.first.profile.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Data
public class ProfileDto {
    private UUID id;
    private String login;
    private String name;
    private String surname;
    private String patronymic;
    private Date birthDay;
    private Date registerDate;
    private String password;
}
