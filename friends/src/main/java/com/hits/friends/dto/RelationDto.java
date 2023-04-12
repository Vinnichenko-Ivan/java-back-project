package com.hits.friends.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.UUID;

@Data
public class RelationDto {
    private Date dateStart;

    private Date dateEnd;

    private UUID mainUser;

    private UUID targetUser;

    private String nameTarget;
    private String surnameTarget;
    private String patronymicTarget;
}
