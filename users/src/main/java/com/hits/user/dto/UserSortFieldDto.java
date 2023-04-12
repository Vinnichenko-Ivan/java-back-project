package com.hits.user.dto;

import com.hits.common.enums.SortType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.Date;

@Data
public class UserSortFieldDto {
    private Sort.Direction loginSD;
    private Sort.Direction emailSD;
    private Sort.Direction nameSD;
    private Sort.Direction surnameSD;
    private Sort.Direction patronymicSD;
    private Sort.Direction phoneSD;
    private Sort.Direction citySD;
    private Sort.Direction birthDateSD;
    private Sort.Direction registrationDateSD;

}
