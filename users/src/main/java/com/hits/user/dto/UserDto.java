package com.hits.user.dto;

import com.hits.common.dto.user.FullNameDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.UUID;

import static com.hits.user.config.RegexConfig.LOGIN;

@Data
public class UserDto {
    @NotNull
    @Pattern(regexp = LOGIN)
    private String login;
    private FullNameDto fullName;
    private Date birthDate;
    private Date registrationDate;
}
