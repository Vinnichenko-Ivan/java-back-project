package com.hits.notification.dto;

import com.hits.common.enums.NotificationType;
import lombok.Data;

import java.sql.Date;
import java.util.Set;

@Data
public class NotificationFilterDto {
    private Date start;
    private Date end;
    private String filter;
    private Set<NotificationType> filterType;
}
