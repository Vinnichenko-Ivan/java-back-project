package com.hits.user.controller;

import com.hits.common.dto.user.FullNameDto;
import com.hits.common.exception.NotImplementedException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.Utils;
import com.hits.user.dto.UserDto;
import com.hits.user.dto.UserEditDto;
import com.hits.user.service.CommonService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.UUID;

import static com.hits.common.Paths.*;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class CommonController {
    private final CommonService commonService;

    private final ApiKeyProvider apiKeyProvider;

    @GetMapping(USERS_COMMON_CHECK_USER)
    public Boolean checkUser(@PathVariable UUID id, @RequestHeader(API_SECURE_HEADER) String key)
    {
        Utils.logQuery(USERS_COMMON_CHECK_USER, id);
        apiKeyProvider.checkKey(key);
        return commonService.checkUser(id);
    }

    @GetMapping(USERS_COMMON_GET_USER_NAME)
    public FullNameDto getUserName(@PathVariable UUID id, @RequestHeader(API_SECURE_HEADER) String key) {
        Utils.logQuery(USERS_COMMON_GET_USER_NAME, id);
        apiKeyProvider.checkKey(key);
        return commonService.getUserName(id);
    }
}
