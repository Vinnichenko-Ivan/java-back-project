package com.hits.friends.controller;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.dto.user.NameSyncDto;
import com.hits.common.exception.NotImplementedException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.friends.service.CommonService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/friends/common")
@RequiredArgsConstructor
@Validated
public class CommonController {

    private final ApiKeyProvider apiKeyProvider;

    private final CommonService commonService;

    @PatchMapping(value = "/synchronise")
    void synchronise(@Valid @RequestBody NameSyncDto nameSyncDto, @RequestHeader("api-key") String key) {
        apiKeyProvider.checkKey(key);
        commonService.synchronise(nameSyncDto);
    }

    @PostMapping(value = "blocking")
    Boolean checkBlocking(@Valid @RequestBody CheckDto checkDto, @RequestHeader("api-key") String key) {
        apiKeyProvider.checkKey(key);
        return commonService.checkBlocking(checkDto);
    }
}
