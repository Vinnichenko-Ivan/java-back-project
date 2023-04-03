package com.hits.user.controller;

import com.hits.user.dto.*;
import com.hits.user.exception.NotImplementedException;
import com.hits.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto)
    {
        throw new NotImplementedException();
    }

    @PostMapping("/sign-in")
    public UserDto singIn(@RequestBody CredentialsDto credentialsDto)
    {
        throw new NotImplementedException();
    }

    @GetMapping("/users")
    public UsersDto getUsers(@RequestBody UsersQueryDto usersQueryDto)
    {
        throw new NotImplementedException();
    }

    @GetMapping("/user")
    public UserDto getUser(String login)
    {
        throw new NotImplementedException();
    }

    @GetMapping("/me")
    public UserDto getMe()
    {
        throw new NotImplementedException();
    }

    @PutMapping("/me")
    public UserDto putMe(@RequestBody UserEditDto userEditDto)
    {
        throw new NotImplementedException();
    }

}
