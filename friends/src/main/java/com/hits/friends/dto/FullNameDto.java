package com.hits.friends.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FullNameDto {
    @NotBlank
    @ApiModelProperty(notes = "name", example = "Ivan", required = true)
    private String name;

    @NotBlank
    @ApiModelProperty(notes = "surname", example = "Ivanov", required = true)
    private String surname;

    @NotBlank
    @ApiModelProperty(notes = "patronymic", example = "Ivanovich", required = true)
    private String patronymic;
}
