package com.hits.notification.service.impl;

import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.common.exception.NotFoundException;
import com.hits.common.exception.NotImplementedException;
import com.hits.common.service.JwtProvider;
import com.hits.notification.dto.NotificationsDto;
import com.hits.notification.dto.NotificationsQueryDto;
import com.hits.notification.dto.ReadDto;
import com.hits.notification.mapper.NotificationMapper;
import com.hits.notification.model.Notification;
import com.hits.notification.model.NotificationNotReadDto;
import com.hits.notification.repository.NotificationRepository;
import com.hits.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final JwtProvider jwtProvider;

    @Override
    public void saveNotification(CreateNotificationDto createNotificationDto) {
        Notification notification = notificationMapper.map(createNotificationDto);
        notificationRepository.save(notification);
    }

    @Override
    public NotificationsDto getNotifications(NotificationsQueryDto notificationsQueryDto) {
        throw new NotImplementedException();
    }

    @Override
    public Long notRead() {
        return notificationRepository.count(new NotificationNotReadDto(jwtProvider.getId()));
    }

    @Override
    @Transactional
    public Long read(ReadDto readDto) {
        List<Notification> notifications = new ArrayList<>();
        for(UUID id : readDto.getIds()) {
            Notification notification = notificationRepository.findById(id).orElseThrow(NotFoundException::new);
            notification.setNotificationStatus(readDto.getStatus());
            notifications.add(notification);
        }
        log.info("Notification status updated:" + notifications.toString());
        notificationRepository.saveAll(notifications);

        return notRead();
    }
}
