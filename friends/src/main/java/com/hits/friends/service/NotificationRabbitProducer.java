package com.hits.friends.service;

import com.hits.common.dto.user.FullNameDto;

import java.util.UUID;

public interface NotificationRabbitProducer {
    void sendNewUserNotify(UUID userId, FullNameDto friendName);
}
