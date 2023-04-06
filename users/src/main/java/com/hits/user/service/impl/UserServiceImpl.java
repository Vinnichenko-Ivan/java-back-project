package com.hits.user.service.impl;

import com.hits.user.dto.UserRegisterDto;
import com.hits.user.exception.BadCredentialsException;
import com.hits.user.exception.UserAlreadyExistException;
import com.hits.user.dto.CredentialsDto;
import com.hits.user.dto.UserDto;
import com.hits.user.dto.UserEditDto;
import com.hits.user.mapper.UserMapper;
import com.hits.user.model.User;
import com.hits.user.repository.UserRepository;
import com.hits.user.service.JwtService;
import com.hits.user.service.PasswordService;
import com.hits.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordService passwordService;


    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if(userRepository.existsByLogin(userRegisterDto.getLogin())) {
            throw new UserAlreadyExistException("login is used");
        }
        else if(userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new UserAlreadyExistException("email is used");
        }
        else {
            User user = userMapper.map(userRegisterDto);
            user.setPassword(passwordService.toHash(user.getPassword()));
            user = userRepository.save(user);
            return userMapper.map(user);
        }
    }

    @Override
    public ResponseEntity<UserDto> authorize(CredentialsDto credentialsDto) {
        if(userRepository.existsByLogin(credentialsDto.getLogin())) {
            User user = userRepository.getByLogin(credentialsDto.getLogin());
            if(passwordService.comparison(credentialsDto.getPassword(), user.getPassword()))
            {
                UserDto userDto = userMapper.map(user);
                String token = jwtService.generateAccessToken(user);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("Authorization", "Bearer " + token);
                return new ResponseEntity(userDto, responseHeaders, HttpStatus.OK);
            }
            else
            {
                throw new BadCredentialsException();
            }
        }
        else {
            throw new BadCredentialsException();
        }
    }

    @Override
    public void putUser(UserEditDto userEditDto) {
        if(userRepository.existsByLogin(userEditDto.getLogin())) {
            if(userRepository.existsByLogin(userEditDto.getLogin())) {
                throw new UserAlreadyExistException("login is used");
            }
            else if(userRepository.existsByEmail(userEditDto.getEmail())) {
                throw new UserAlreadyExistException("email is used");
            }
            User user = userRepository.getByLogin(userEditDto.getLogin());
            if(user.getPassword().equals(userEditDto.getPassword()))
            {
                userMapper.map(user, userEditDto);
                userRepository.save(user);
            }
            else
            {
                throw new BadCredentialsException();
            }
        }
        else {
            throw new BadCredentialsException();
        }
    }

    @Override
    public UserDto getMe() {
        return userMapper.map(jwtService.getUser());
    }
}
