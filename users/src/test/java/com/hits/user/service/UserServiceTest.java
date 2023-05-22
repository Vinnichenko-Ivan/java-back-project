package com.hits.user.service;

import com.hits.common.dto.user.FullNameDto;
import com.hits.user.dto.*;
import com.hits.user.mapper.UserMapper;
import com.hits.user.repository.UserRepository;
import com.hits.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void registerTest() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        FullNameDto fullNameDto = new FullNameDto();
        fullNameDto.setName("test1");
        fullNameDto.setPatronymic("test2");
        fullNameDto.setSurname("test3");
        userRegisterDto.setAvatarId(UUID.randomUUID());
        userRegisterDto.setBirthDate(new Date(2123123));
        userRegisterDto.setCity("Tomsk");
        userRegisterDto.setEmail("email@email.com");
        userRegisterDto.setFullName(fullNameDto);
        userRegisterDto.setLogin("testlogin");
        userRegisterDto.setPhone("89234091234");

        when(userRepository.existsByLogin(any())).thenReturn(true);
        when(userRepository.existsByLogin("testlogin")).thenReturn(false);

        when(userRepository.existsByEmail(any())).thenReturn(true);
        when(userRepository.existsByEmail("email@email.com")).thenReturn(false);

        UserDto dto = userService.register(userRegisterDto);


    }

    @Test
    public void authorizeTest() {

    }

    @Test
    public void putUserTest() {

    }

    @Test
    public void getMeTest() {

    }

    @Test
    public void getUserTest() {

    }

    @Test
    public void getUsersTest() {

    }
}
