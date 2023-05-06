package com.hits.chat.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class Message {
    @Id
    private UUID id;

    private UUID authorId;

    @ManyToOne
    private Chat chat;

    private Date createdDate;

    @ElementCollection
    private Set<File> files;

    private String text;

    @PrePersist
    public void generate()
    {
        this.createdDate = new Date(System.currentTimeMillis());
        this.id = java.util.UUID.randomUUID();
    }
}
