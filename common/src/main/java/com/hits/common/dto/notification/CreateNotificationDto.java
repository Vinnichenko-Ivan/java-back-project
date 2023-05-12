package com.hits.common.dto.notification;

import com.hits.common.enums.NotificationType;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateNotificationDto {
    private UUID userId;
    private NotificationType notificationType;
    private String text;
}
