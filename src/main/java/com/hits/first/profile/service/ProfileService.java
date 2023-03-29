package com.hits.first.profile.service;

import com.hits.first.exception.NotImplementedException;
import com.hits.first.profile.dto.CredentialsDto;
import com.hits.first.profile.dto.ProfileDto;
import com.hits.first.profile.dto.ProfileEditDto;
import com.hits.first.profile.dto.ProfileRegisterDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public interface ProfileService {

    void register(ProfileRegisterDto profileRegisterDto);

    ProfileDto getUser(CredentialsDto credentialsDto);

    void putUser(ProfileEditDto profileEditDto);

}

