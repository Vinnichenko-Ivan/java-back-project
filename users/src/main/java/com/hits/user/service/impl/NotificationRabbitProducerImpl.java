package com.hits.user.service.impl;

import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.common.enums.NotificationType;
import com.hits.common.service.Utils;
import com.hits.user.service.NotificationRabbitProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationRabbitProducerImpl implements NotificationRabbitProducer {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendNewUserNotify(UUID userId, String login) {
        CreateNotificationDto dto = createNotificationDto(userId, login);
        Utils.logRabbitCreateNotification(dto);
        rabbitTemplate.convertAndSend("standart_notify","", dto);
    }

    private CreateNotificationDto createNotificationDto(UUID userId, String login) {
        CreateNotificationDto notificationDto = new CreateNotificationDto();
        notificationDto.setUserId(userId);
        notificationDto.setText("В ваш акаунт " + login + " был выполнен вход.");
        notificationDto.setNotificationType(NotificationType.LOGIN);
        return notificationDto;
    }
}
