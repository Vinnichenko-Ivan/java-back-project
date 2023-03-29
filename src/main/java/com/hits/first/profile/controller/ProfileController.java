package com.hits.first.profile.controller;

import com.hits.first.exception.NotImplementedException;
import com.hits.first.profile.dto.CredentialsDto;
import com.hits.first.profile.dto.ProfileDto;
import com.hits.first.profile.dto.ProfileEditDto;
import com.hits.first.profile.dto.ProfileRegisterDto;
import com.hits.first.profile.service.ProfileService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/sign-up")
    public void register(ProfileRegisterDto profileRegisterDto)
    {
        profileService.register(profileRegisterDto);
    }

    @GetMapping("/user")
    public ProfileDto getUser(CredentialsDto credentialsDto)
    {
        return profileService.getUser(credentialsDto);
    }

    @PutMapping("/user")
    public void putUser(ProfileEditDto profileEditDto)
    {
        profileService.putUser(profileEditDto);
    }
}
