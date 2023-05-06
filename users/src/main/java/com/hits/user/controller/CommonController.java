package com.hits.user.controller;

import com.hits.common.dto.user.FullNameDto;
import com.hits.common.exception.NotImplementedException;
import com.hits.common.service.ApiKeyProvider;
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

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "users/common")
@RequiredArgsConstructor
@Validated
public class CommonController {
    private final CommonService commonService;

    private final ApiKeyProvider apiKeyProvider;

    @GetMapping("/check/{id}")
    public Boolean checkUser(@PathVariable UUID id, @RequestHeader("api-key") String key)
    {
        apiKeyProvider.checkKey(key);
        return commonService.checkUser(id);
    }

    @GetMapping("/user/{id}")
    public FullNameDto getUserName(@PathVariable UUID id, @RequestHeader("api-key") String key) {
        return commonService.getUserName(id);
    }
}
