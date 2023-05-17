package com.hits.notification.service;

import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.common.exception.NotImplementedException;
import com.hits.notification.dto.NotificationsDto;
import com.hits.notification.dto.NotificationsQueryDto;
import com.hits.notification.dto.ReadDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface NotificationService {
    void saveNotification(CreateNotificationDto createNotificationDto);

    NotificationsDto getNotifications(NotificationsQueryDto notificationsQueryDto);

    Long notRead();
    Long read(ReadDto readDto);

}
