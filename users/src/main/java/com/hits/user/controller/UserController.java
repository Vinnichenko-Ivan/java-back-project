package com.hits.user.controller;

import com.hits.common.exception.NotImplementedException;
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

import static com.hits.user.config.RegexConfig.LOGIN;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public UserDto register(@Valid @RequestBody UserRegisterDto userRegisterDto)
    {
        return userService.register(userRegisterDto);
    }

    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> singIn(@Valid @RequestBody CredentialsDto credentialsDto)
    {
        return userService.authorize(credentialsDto);
    }

    @PostMapping("/users")
    public UsersDto getUsers(@Valid @RequestBody UsersQueryDto usersQueryDto)
    {
        return userService.getUsers(usersQueryDto); //TODO фильтры
    }

    @GetMapping("/user/{login}")
    public UserDto getUser(@Valid @NotNull @Pattern(regexp = LOGIN) @PathVariable String login)
    {
        return userService.getUser(login);
    }

    @GetMapping("/me")
    public UserDto getMe()
    {
        return userService.getMe();
    }

    @PutMapping("/me")
    public UserDto putMe(@Valid @RequestBody UserEditDto userEditDto)
    {
        return userService.putUser(userEditDto);
    }

}
