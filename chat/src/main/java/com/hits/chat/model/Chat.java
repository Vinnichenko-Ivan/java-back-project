package com.hits.chat.model;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Chat {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    private UUID adminUser;

    private String name;

    private Date createdDate;

    private UUID avatarId;

    private Date lastMessageDate = null;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<UUID> users;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> messages;

    @PrePersist
    public void generate()
    {
        this.createdDate = new Date(System.currentTimeMillis());
        this.id = java.util.UUID.randomUUID();
        this.messages = new ArrayList<>();
    }
}
