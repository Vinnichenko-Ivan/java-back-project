package com.hits.notification.dto.mapper;

import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.common.enums.NotificationType;
import com.hits.notification.dto.NotificationShortDto;
import com.hits.notification.model.Notification;
import com.hits.notification.model.NotificationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    Notification map(CreateNotificationDto createNotificationDto);

    @Mapping(target = "type", source = "notificationType")
    @Mapping(target = "status", source = "notificationStatus")
    NotificationShortDto map(Notification notification);

    default String map(Date date) {
        if(date == null) {
            return null;
        }
        return String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM", date);
    }
}
