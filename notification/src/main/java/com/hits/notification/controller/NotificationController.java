package com.hits.notification.controller;

import com.hits.common.exception.NotImplementedException;
import com.hits.notification.dto.NotificationsDto;
import com.hits.notification.dto.NotificationsQueryDto;
import com.hits.notification.dto.ReadDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/notification/")
@RequiredArgsConstructor
@Validated
public class NotificationController {


    public NotificationsDto getNotifications(NotificationsQueryDto notificationsQueryDto) {
        throw new NotImplementedException();
    }

    public Integer notRead() {
        throw new NotImplementedException();
    }

    public Integer read(ReadDto readDto) {
        throw new NotImplementedException();
    }
}
