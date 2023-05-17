package com.hits.notification.service;

import com.hits.common.dto.notification.CreateNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
@RequiredArgsConstructor
@Log4j2
public class RabbitMQConsumer {

    private final NotificationService  notificationService;

    @RabbitListener(queues = "queue_notif")
    public void processQueueNotif(CreateNotificationDto createNotificationDto) {
        log.info("Received from queue_notif : " + createNotificationDto.toString());
        notificationService.saveNotification(createNotificationDto);
    }
}
