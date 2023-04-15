package com.hits.friends.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class QueryRelationSort {
    private Sort.Direction nameSD;
    private Sort.Direction surnameSD;
    private Sort.Direction patronymicSD;
    private Sort.Direction dateSD;
}
