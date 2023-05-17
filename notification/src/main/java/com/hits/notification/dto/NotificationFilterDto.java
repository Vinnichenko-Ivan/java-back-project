package com.hits.notification.dto;

import com.hits.common.enums.NotificationType;
import lombok.Data;

import java.sql.Date;

@Data
public class NotificationFilterDto {
    private Date start;
    private Date end;
    private String filter;
    private NotificationType filterType;
}
