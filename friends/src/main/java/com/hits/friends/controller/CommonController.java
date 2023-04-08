package com.hits.friends.controller;

import com.hits.friends.dto.FriendDto;
import com.hits.friends.dto.NameSyncDto;
import com.hits.friends.exception.NotImplementedException;
import com.hits.friends.service.CommonService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class CommonController {

    private final CommonService commonService;

    @PatchMapping(value = "/synchronise")
    void synchronise(NameSyncDto nameSyncDto) {
        commonService.synchronise(nameSyncDto);
    }
}
