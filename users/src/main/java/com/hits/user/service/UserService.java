package com.hits.user.service;

import com.hits.user.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);
    ResponseEntity<UserDto> authorize(CredentialsDto credentialsDto);
    UserDto putUser(UserEditDto userEditDto);
    UserDto getMe();

    UsersDto getUsers(UsersQueryDto usersQueryDto);
}

