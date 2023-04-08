package com.hits.user.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.UUID;

import static com.hits.user.config.RegexConfig.*;

@Data
@Validated
public class UserFiltersDto {
    @NotNull
    @Pattern(regexp = LOGIN)
    private String login;

    @NotNull
    @Pattern(regexp = EMAIL)
    private String email;

    private FullNameDto fullName;
    
    @NotNull
    @Pattern(regexp = PHONE)
    private String phone;

    private String city;

    private UUID avatarId;
}
