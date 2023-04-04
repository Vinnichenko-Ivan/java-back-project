package com.hits.user.service;

import com.hits.user.dto.CredentialsDto;
import com.hits.user.dto.UserEditDto;
import com.hits.user.dto.UserDto;
import com.hits.user.dto.UserRegisterDto;

public interface UserService {

    UserDto register(UserRegisterDto userRegisterDto);

    UserDto getUser(CredentialsDto credentialsDto);

    void putUser(UserEditDto userEditDto);

}

