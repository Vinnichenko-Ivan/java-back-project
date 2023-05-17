package com.hits.notification.model;

import com.hits.common.enums.NotificationType;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Notification {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String text;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    private Date createdDate;

    private Date readDate;

    @PrePersist
    public void generate()
    {
        this.createdDate = new Date(System.currentTimeMillis());
        this.id = java.util.UUID.randomUUID();
    }
}
