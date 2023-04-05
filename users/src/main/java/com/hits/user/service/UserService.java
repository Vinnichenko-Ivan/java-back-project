package com.hits.user.service;

import com.hits.user.dto.CredentialsDto;
import com.hits.user.dto.UserEditDto;
import com.hits.user.dto.UserDto;
import com.hits.user.dto.UserRegisterDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);
    ResponseEntity<UserDto> authorize(CredentialsDto credentialsDto);
    void putUser(UserEditDto userEditDto);

}

