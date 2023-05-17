package com.hits.notification.dto;

import com.hits.common.dto.user.PaginationQueryDto;
import lombok.Data;

@Data
public class NotificationsQueryDto {
    private NotificationFilterDto notificationFilterDto;
    private PaginationQueryDto paginationQueryDto;
}
