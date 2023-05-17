package com.hits.notification.dto;

import com.hits.common.enums.NotificationType;
import com.hits.notification.model.NotificationStatus;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class NotificationShortDto {
    private UUID id;
    private NotificationType type;
    private String text;
    private NotificationStatus status;
    private Date createdDate;
}
