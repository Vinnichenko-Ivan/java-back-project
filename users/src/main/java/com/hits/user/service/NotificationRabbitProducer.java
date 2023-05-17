package com.hits.user.service;

import com.hits.common.dto.user.FullNameDto;

import java.util.UUID;

public interface NotificationRabbitProducer {
    void sendNewUserNotify(UUID userId, String login);
}
