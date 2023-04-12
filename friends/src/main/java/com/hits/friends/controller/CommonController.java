package com.hits.friends.controller;

import com.hits.common.dto.user.NameSyncDto;
import com.hits.common.service.ApiKeyProvider;
import com.hits.friends.service.CommonService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/common")
@RequiredArgsConstructor
@Validated
public class CommonController {

    private final ApiKeyProvider apiKeyProvider;

    private final CommonService commonService;

    @PatchMapping(value = "/synchronise")
    void synchronise(NameSyncDto nameSyncDto, @RequestHeader("api-key") String key) {
        apiKeyProvider.checkKey(key);
        commonService.synchronise(nameSyncDto);
    }
}
