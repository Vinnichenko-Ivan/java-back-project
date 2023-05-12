package com.hits.common.dto.user;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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

    @Override
    public String toString() {
        return surname + " " + name + " " + surname;
    }
}
