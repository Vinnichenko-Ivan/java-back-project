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
    public void sendNewUserFriendNotify(UUID userId, FullNameDto friendName) {
        log.info("notify");
        rabbitTemplate.convertAndSend("standart_notify","", createNotificationDto(userId, friendName, NotificationType.FRIEND_ADD));
    }

    @Override
    public void sendDeleteUserFriendNotify(UUID userId, FullNameDto friendName) {
        log.info("notify");
        rabbitTemplate.convertAndSend("standart_notify","", createNotificationDto(userId, friendName, NotificationType.FRIEND_DELETE));
    }

    @Override
    public void sendNewUserBlockNotify(UUID userId, FullNameDto friendName) {
        log.info("notify");
        rabbitTemplate.convertAndSend("standart_notify","", createNotificationDto(userId, friendName, NotificationType.BLOCK_ADD));
    }

    @Override
    public void sendDeleteUserBlockNotify(UUID userId, FullNameDto friendName) {
        log.info("notify");
        rabbitTemplate.convertAndSend("standart_notify","", createNotificationDto(userId, friendName, NotificationType.BLOCK_DELETE));
    }

    private CreateNotificationDto createNotificationDto(UUID userId, FullNameDto friendName, NotificationType type) {
        CreateNotificationDto createNotificationDto = new CreateNotificationDto();
        createNotificationDto.setUserId(userId);
        createNotificationDto.setNotificationType(type);

        switch (type) {
            case FRIEND_ADD:
                createNotificationDto.setText(getTextAddFriend(friendName));
                break;
            case FRIEND_DELETE:
                createNotificationDto.setText(getTextDeleteFriend(friendName));
                break;
            case BLOCK_ADD:
                createNotificationDto.setText(getTextAddBlock(friendName));
                break;
            case BLOCK_DELETE:
                createNotificationDto.setText(getTextDeleteBlock(friendName));
                break;
            default:
                break;
        }
        return createNotificationDto;
    }

    private String getTextAddFriend(FullNameDto fullNameDto) {
        return "К вам в друзья добавился " + fullNameDto.toString();
    }

    private String getTextDeleteFriend(FullNameDto fullNameDto) {
        return "Из друзей удалился " + fullNameDto.toString();
    }

    private String getTextAddBlock(FullNameDto fullNameDto) {
        return "Вас заблокировал " + fullNameDto.toString();
    }

    private String getTextDeleteBlock(FullNameDto fullNameDto) {
        return "Вас разблокировал " + fullNameDto.toString();
    }
}
