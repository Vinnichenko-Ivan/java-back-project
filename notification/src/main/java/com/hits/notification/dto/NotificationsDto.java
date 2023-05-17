package com.hits.notification.dto;

import com.hits.common.dto.user.PaginationDto;
import com.hits.notification.model.Notification;
import lombok.Data;

import java.util.List;

@Data
public class NotificationsDto {
    private List<NotificationShortDto> notifications;
    private PaginationDto paginationDto;
}
