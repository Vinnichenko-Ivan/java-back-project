package com.hits.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.hits.user.config.RegexConfig.LOGIN;
import static com.hits.user.config.RegexConfig.PASSWORD;

@Data
public class CredentialsDto {

    @NotNull
    @Pattern(regexp = LOGIN)
    private String login;

    @NotNull
    @Pattern(regexp = PASSWORD)
    private String password;
}
