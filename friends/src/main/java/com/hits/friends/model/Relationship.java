package com.hits.friends.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
public class Relationship {
    @Id
    private UUID id;

    private Date dateStart;

    private Date dateEnd;

    @Column(name = "main_user")
    private UUID mainUser;
    @Column(name = "target_user")
    private UUID targetUser;

    private String nameTarget;
    private String surnameTarget;
    private String patronymicTarget;

    @PrePersist
    public void generate()
    {
        this.dateStart = new Date(System.currentTimeMillis());
        this.id = java.util.UUID.randomUUID();
    }
}
