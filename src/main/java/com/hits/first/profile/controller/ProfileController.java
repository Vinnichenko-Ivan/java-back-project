package com.hits.first.profile.controller;

import com.hits.first.exception.NotImplementedException;
import com.hits.first.profile.dto.ProfileRegisterDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    @PostMapping("/register")
    public void register(ProfileRegisterDto profileRegisterDto)
    {
        throw new NotImplementedException();
    }

}
