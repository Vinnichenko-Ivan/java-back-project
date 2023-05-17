package com.hits.notification.mapper;

import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.notification.model.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    Notification map(CreateNotificationDto createNotificationDto);

}
