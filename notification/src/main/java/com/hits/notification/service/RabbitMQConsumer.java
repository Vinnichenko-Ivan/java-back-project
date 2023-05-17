package com.hits.notification.service;

import com.hits.common.dto.notification.CreateNotificationDto;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class RabbitMQConsumer {


    @RabbitListener(queues = "queue_notif")
    public void processQueueNotif(CreateNotificationDto createNotificationDto) {
        System.out.printf("Received from queue_notif : %s \n", createNotificationDto.toString());
    }
}
