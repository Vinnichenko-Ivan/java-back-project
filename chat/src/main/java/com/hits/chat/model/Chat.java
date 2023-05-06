package com.hits.chat.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @ElementCollection
    private Set<UUID> users;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> messages;

    @PrePersist
    public void generate()
    {
        this.createdDate = new Date(System.currentTimeMillis());
        this.id = java.util.UUID.randomUUID();
    }
}
