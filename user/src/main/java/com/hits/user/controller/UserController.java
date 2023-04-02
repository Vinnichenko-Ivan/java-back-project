package com.hits.user.controller;

import com.hits.user.dto.CredentialsDto;
import com.hits.user.dto.UserEditDto;
import com.hits.user.dto.UserDto;
import com.hits.user.dto.UserRegisterDto;
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
    public void register(UserRegisterDto userRegisterDto)
    {
        userService.register(userRegisterDto);
    }

    @GetMapping("/user")
    public UserDto getUser(CredentialsDto credentialsDto)
    {
        return userService.getUser(credentialsDto);
    }

    @PutMapping("/user")
    public void putUser(UserEditDto userEditDto)
    {
        userService.putUser(userEditDto);
    }
}
