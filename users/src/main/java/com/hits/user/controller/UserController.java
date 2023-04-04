package com.hits.user.controller;

import com.hits.user.dto.*;
import com.hits.user.exception.NotImplementedException;
import com.hits.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.hits.user.config.RegexConfig.LOGIN;

@Api
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public UserDto singIn(@Valid @RequestBody CredentialsDto credentialsDto)
    {
        throw new NotImplementedException();
    }

    @GetMapping("/users")
    public UsersDto getUsers(@Valid @RequestBody UsersQueryDto usersQueryDto)
    {
        throw new NotImplementedException();
    }

    @PostMapping("/user")
    public UserDto getUser(@Valid @NotNull @Pattern(regexp = LOGIN) String login)
    {
        throw new NotImplementedException();
    }

    @GetMapping("/me")
    public UserDto getMe()
    {
        throw new NotImplementedException();
    }

    @PutMapping("/me")
    public UserDto putMe(@Valid @RequestBody UserEditDto userEditDto)
    {
        throw new NotImplementedException();
    }

}
