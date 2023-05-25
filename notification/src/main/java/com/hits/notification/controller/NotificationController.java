package com.hits.notification.controller;

import com.hits.common.exception.NotImplementedException;
import com.hits.common.service.Utils;
import com.hits.notification.dto.NotificationsDto;
import com.hits.notification.dto.NotificationsQueryDto;
import com.hits.notification.dto.ReadDto;
import com.hits.notification.model.Notification;
import com.hits.notification.service.NotificationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.hits.common.Paths.*;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping(value = NOTIFICATION_GET)
    public NotificationsDto getNotifications(@RequestBody @Valid NotificationsQueryDto notificationsQueryDto) {
        Utils.logQuery(NOTIFICATION_GET, notificationsQueryDto);
        return notificationService.getNotifications(notificationsQueryDto);
    }

    @GetMapping(value = NOTIFICATION_NOT_READ)
    public Long notRead() {
        Utils.logQuery(NOTIFICATION_NOT_READ);
        return notificationService.notRead();
    }

    @PostMapping(value = NOTIFICATION_READ)
    public Long read(@RequestBody @Valid ReadDto readDto) {
        Utils.logQuery(NOTIFICATION_READ, readDto);
        return notificationService.read(readDto);
    }
}
