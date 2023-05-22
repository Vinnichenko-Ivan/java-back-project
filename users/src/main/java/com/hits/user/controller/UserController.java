package com.hits.user.controller;

import com.hits.common.service.Utils;
import com.hits.user.dto.*;
import com.hits.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.hits.common.Paths.*;
import static com.hits.user.config.RegexConfig.LOGIN;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping(USERS_SIGN_UP)
    public UserDto register(@Valid @RequestBody UserRegisterDto userRegisterDto)
    {
        Utils.logQuery(USERS_SIGN_UP, userRegisterDto);
        return userService.register(userRegisterDto);
    }

    @PostMapping(value = USERS_SIGN_IN, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> singIn(@Valid @RequestBody CredentialsDto credentialsDto)
    {
        Utils.logQuery(USERS_SIGN_IN, "Я не покажу тебе пароль или логин тут");
        return userService.authorize(credentialsDto);
    }

    @PostMapping(USERS_GET_USERS)
    public UsersDto getUsers(@Valid @RequestBody UsersQueryDto usersQueryDto)
    {
        Utils.logQuery(USERS_GET_USERS, usersQueryDto);
        return userService.getUsers(usersQueryDto); //TODO фильтры
    }

    @GetMapping(USERS_GET_USER)
    public UserDto getUser(@Valid @NotNull @Pattern(regexp = LOGIN) @PathVariable String login)
    {
        Utils.logQuery(USERS_GET_USER, login);
        return userService.getUser(login);
    }

    @GetMapping(USERS_GET_ME)
    public UserDto getMe()
    {
        Utils.logQuery(USERS_GET_ME);
        return userService.getMe();
    }

    @PutMapping(USERS_PUT_ME)
    public UserDto putMe(@Valid @RequestBody UserEditDto userEditDto)
    {
        Utils.logQuery(USERS_PUT_ME);
        return userService.putUser(userEditDto);
    }

}
