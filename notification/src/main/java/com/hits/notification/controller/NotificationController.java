package com.hits.notification.controller;

import com.hits.common.exception.NotImplementedException;
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

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/notification/")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping(value = "")
    public NotificationsDto getNotifications(@RequestBody @Valid NotificationsQueryDto notificationsQueryDto) {
        return notificationService.getNotifications(notificationsQueryDto);
    }

    @GetMapping(value = "not-read")
    public Long notRead() {
        return notificationService.notRead();
    }

    @PostMapping(value = "read")
    public Long read(@RequestBody @Valid ReadDto readDto) {
        return notificationService.read(readDto);
    }
}
