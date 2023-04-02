package com.hits.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CredentialsDto {
    @NotEmpty(message = "login.empty")
    private String login;

    @NotEmpty(message = "password.empty")
    private String password;
}
