package com.hits.notification.service.impl;

import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.notification.mapper.NotificationMapper;
import com.hits.notification.model.Notification;
import com.hits.notification.repository.NotificationRepository;
import com.hits.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    @Override
    public void saveNotification(CreateNotificationDto createNotificationDto) {
        Notification notification = notificationMapper.map(createNotificationDto);
        notificationRepository.save(notification);
    }
}
