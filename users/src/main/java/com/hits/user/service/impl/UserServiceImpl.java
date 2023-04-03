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
import com.hits.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        if(userRepository.existsByLogin(userRegisterDto.getLogin()) || userRepository.existsByPhone(userRegisterDto.getPhone())) {
            throw new UserAlreadyExistException();
        }
        else {
            User user = userMapper.map(userRegisterDto);
            userRepository.save(user);
        }
    }

    @Override
    public UserDto getUser(CredentialsDto credentialsDto) {
        if(userRepository.existsByLogin(credentialsDto.getLogin())) {
            User user = userRepository.getByLogin(credentialsDto.getLogin());
            if(user.getPassword().equals(credentialsDto.getPassword()))
            {
                return userMapper.map(user);
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
            if(userRepository.existsByLogin(userEditDto.getLogin()) || userRepository.existsByPhone(userEditDto.getPhone())) {
                throw new UserAlreadyExistException();
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
}
