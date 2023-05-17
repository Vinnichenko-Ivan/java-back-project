package com.hits.notification.service;

import com.hits.common.dto.notification.CreateNotificationDto;

public interface NotificationService {
    void saveNotification(CreateNotificationDto createNotificationDto);

}
