package com.hits.friends.dto;

import lombok.Data;

import java.util.Date;

@Data
public class QueryRelationFilter {
    private String name;
    private String surname;
    private String patronymic;
    private Date startDate; //TODO пока не работает
    private Date endDate;
}
