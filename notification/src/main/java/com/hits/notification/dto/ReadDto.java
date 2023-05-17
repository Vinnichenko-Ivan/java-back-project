package com.hits.notification.dto;

import com.hits.notification.model.NotificationStatus;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ReadDto {
    private List<UUID> ids;
    private NotificationStatus status;
}
