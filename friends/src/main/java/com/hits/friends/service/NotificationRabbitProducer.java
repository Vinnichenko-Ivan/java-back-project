package com.hits.friends.service;

import com.hits.common.dto.user.FullNameDto;

import java.util.UUID;

public interface NotificationRabbitProducer {
    void sendNewUserFriendNotify(UUID userId, FullNameDto friendName);

    void sendDeleteUserFriendNotify(UUID userId, FullNameDto friendName);

    void sendNewUserBlockNotify(UUID userId, FullNameDto friendName);

    void sendDeleteUserBlockNotify(UUID userId, FullNameDto friendName);
}
