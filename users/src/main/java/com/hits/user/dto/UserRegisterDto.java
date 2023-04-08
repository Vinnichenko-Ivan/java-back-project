package com.hits.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.UUID;

import static com.hits.user.config.RegexConfig.*;

@Data
public class UserRegisterDto {
    @NotNull
    @Pattern(regexp = LOGIN)
    @ApiModelProperty(notes = "login", example = "term123", required = true)
    private String login;

    @NotNull
    @Pattern(regexp = EMAIL)
    @ApiModelProperty(notes = "email", example = "jora@gmail.com", required = true)
    private String email;

    @NotNull
    @Pattern(regexp = PASSWORD)
    @ApiModelProperty(notes = "password", example = "Strong@12", required = true)
    private String password;

    @NotNull
    private FullNameDto fullName;

    @ApiModelProperty(notes = "birthDate", example = "2003-04-04T04:44:44.290Z", required = false)
    private Date birthDate;

    @NotNull
    @Pattern(regexp = PHONE)
    @ApiModelProperty(notes = "phone", example = "88005553535", required = false)
    private String phone;

    @ApiModelProperty(notes = "city", example = "Tomsk", required = false)
    private String city;

    @ApiModelProperty(notes = "avatarId", example = "59da80cd-a7e4-422c-b6fb-0bf2b0dc5a0c", required = false)
    private UUID avatarId;
}
