package com.hits.user.dto;

import com.hits.common.dto.user.FullNameDto;
import io.swagger.annotations.ApiModelProperty;
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
    private String login;

    private String email;

    @ApiModelProperty(notes = "name", example = "Ivan")
    private String name;

    @ApiModelProperty(notes = "surname", example = "Ivanov")
    private String surname;

    @ApiModelProperty(notes = "patronymic", example = "Ivanovich")
    private String patronymic;

    private String phone;

    private String city;

    private Date birthDate;

    private Date registrationDate;
}
