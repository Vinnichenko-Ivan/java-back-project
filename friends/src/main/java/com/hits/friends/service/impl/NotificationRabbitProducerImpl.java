package com.hits.friends.service.impl;

import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.common.dto.user.FullNameDto;
import com.hits.common.enums.NotificationType;
import com.hits.friends.service.NotificationRabbitProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationRabbitProducerImpl implements NotificationRabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendNewUserNotify(UUID userId, FullNameDto friendName) {
        log.info("notify");
        rabbitTemplate.convertAndSend("standart_notify","", createNotificationDto(userId, friendName));
    }

    private CreateNotificationDto createNotificationDto(UUID userId, FullNameDto friendName) {
        CreateNotificationDto createNotificationDto = new CreateNotificationDto();
        createNotificationDto.setUserId(userId);
        createNotificationDto.setNotificationType(NotificationType.FRIEND_ADD);
        createNotificationDto.setText(getText(friendName));
        return createNotificationDto;
    }

    private String getText(FullNameDto fullNameDto) {
        return "К вам в друзья добавился " + fullNameDto.toString();
    }
}
