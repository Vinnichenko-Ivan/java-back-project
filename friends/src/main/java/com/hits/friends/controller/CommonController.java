package com.hits.friends.controller;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.dto.user.NameSyncDto;
import com.hits.common.exception.NotImplementedException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.Utils;
import com.hits.friends.service.CommonService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static com.hits.common.Paths.*;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class CommonController {

    private final ApiKeyProvider apiKeyProvider;

    private final CommonService commonService;

    @PatchMapping(value = FRIEND_SYNC)
    void synchronise(@Valid @RequestBody NameSyncDto nameSyncDto, @RequestHeader(API_SECURE_HEADER) String key) {
        Utils.logQuery(FRIEND_SYNC, nameSyncDto);
        apiKeyProvider.checkKey(key);
        commonService.synchronise(nameSyncDto);
    }

    @PostMapping(value = FRIEND_BLOCK_CHECK)
    Boolean checkBlocking(@Valid @RequestBody CheckDto checkDto, @RequestHeader(API_SECURE_HEADER) String key) {
        Utils.logQuery(FRIEND_BLOCK_CHECK, checkDto);
        apiKeyProvider.checkKey(key);
        return commonService.checkBlocking(checkDto);
    }
}
