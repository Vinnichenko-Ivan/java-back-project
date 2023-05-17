package com.hits.chat.service;

import com.hits.common.dto.user.FullNameDto;

import java.util.UUID;

public interface NotificationRabbitProducer {
    void sendNewMessageNotify(UUID userId, String nameChannel, String text);
}
