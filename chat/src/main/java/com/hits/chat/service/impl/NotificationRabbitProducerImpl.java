package com.hits.chat.service.impl;

import com.hits.chat.service.NotificationRabbitProducer;
import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.common.enums.NotificationType;
import com.hits.common.service.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationRabbitProducerImpl implements NotificationRabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendNewMessageNotify(UUID userId, String nameChannel, String text) {
        CreateNotificationDto dto = createNotification(userId, nameChannel, text);
        Utils.logRabbitCreateNotification(dto);
        rabbitTemplate.convertAndSend("standart_notify","", dto);
    }

    private CreateNotificationDto createNotification(UUID userId, String nameChannel, String text) {
        CreateNotificationDto notificationDto = new CreateNotificationDto();
        notificationDto.setUserId(userId);
        notificationDto.setNotificationType(NotificationType.NEW_MESSAGE);
        notificationDto.setText(
                "Вам пришло новое сообщение: " + text + "\n В канале:" + nameChannel
        );
        return notificationDto;
    }

}
