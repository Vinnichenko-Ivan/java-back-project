package com.hits.user.service.impl;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.dto.user.PaginationDto;
import com.hits.common.exception.AlreadyExistException;
import com.hits.common.exception.ExternalServiceErrorException;
import com.hits.common.exception.ForbiddenException;
import com.hits.common.exception.NotFoundException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.JwtProvider;
import com.hits.common.service.Utils;
import com.hits.user.dto.*;
import com.hits.user.exception.BadCredentialsException;
import com.hits.user.mapper.UserMapper;
import com.hits.user.model.User;
import com.hits.user.model.UserSpecification;
import com.hits.user.model.User_;
import com.hits.user.repository.UserRepository;
import com.hits.user.service.FriendService;
import com.hits.user.service.JwtService;
import com.hits.user.service.PasswordService;
import com.hits.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordService passwordService;

    private final FriendService friendService;

    private final ApiKeyProvider apiKeyProvider;

    private final JwtProvider jwtProvider;

    private final NotificationRabbitProducerImpl notificationRabbitProducer;


    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if(userRepository.existsByLogin(userRegisterDto.getLogin())) {
            throw new AlreadyExistException("login is used");
        }
        else if(userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new AlreadyExistException("email is used");
        }
        else {
            User user = userMapper.map(userRegisterDto);
            user.setPassword(passwordService.toHash(userRegisterDto.getPassword()));
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
                try {
                    notificationRabbitProducer.sendNewUserNotify(user.getId(), user.getLogin());
                } catch (Exception e) {
                    throw new ExternalServiceErrorException("rabbit doesn't work");
                }

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

//    @Transactional
    @Override
    public UserDto putUser(UserEditDto userEditDto) {
        UUID id = jwtProvider.getId();
        if(userRepository.existsById(id)) {
            if(userRepository.existsByLogin(userEditDto.getLogin())) {
                throw new AlreadyExistException("login is used");
            }
            else if(userRepository.existsByEmail(userEditDto.getEmail())) {
                throw new AlreadyExistException("email is used");
            }
            User user = userRepository.getById(id);

            userMapper.map(user, userEditDto);
            user = userRepository.save(user);
            try {
                friendService.nameSynchronization(userMapper.mapToSync(user), apiKeyProvider.getKey());
            }
            catch (Exception e)
            {
                throw new ExternalServiceErrorException("friend service error");
            }
            return userMapper.map(user);
        }
        else {
            throw new BadCredentialsException();
        }
    }

    @Override
    public UserDto getMe() {
        return userMapper.map(jwtService.getUser());
    }

    @Override
    public UsersDto getUsers(UsersQueryDto usersQueryDto) {
        UsersDto usersDto = new UsersDto();

        UserFiltersDto userFiltersDto = usersQueryDto.getUserFiltersDto();
        UserSortFieldDto userSortFieldDto = usersQueryDto.getUserSortFieldDto();

        List<Sort.Order> orders = List.of(
                new Sort.Order(userSortFieldDto.getLoginSD(), User_.login.getName()),
                new Sort.Order(userSortFieldDto.getEmailSD(), User_.email.getName()),
                new Sort.Order(userSortFieldDto.getNameSD(), User_.name.getName()),
                new Sort.Order(userSortFieldDto.getSurnameSD(), User_.surname.getName()),
                new Sort.Order(userSortFieldDto.getPatronymicSD(), User_.patronymic.getName()),
                new Sort.Order(userSortFieldDto.getPhoneSD(), User_.phone.getName()),
                new Sort.Order(userSortFieldDto.getCitySD(), User_.city.getName()),
                new Sort.Order(userSortFieldDto.getBirthDateSD(), User_.birthDate.getName()),
                new Sort.Order(userSortFieldDto.getRegistrationDateSD(), User_.registrationDate.getName())
        );

        Page<User> users = userRepository.findAll(new UserSpecification(userFiltersDto), Utils.toPageable(
                usersQueryDto.getPaginationQueryDto(),
                orders
        ));

        usersDto.setUsers(users.stream().map(userMapper::map).collect(Collectors.toList()));
        usersDto.setPaginationDto(Utils.toPagination(users));
        return usersDto;
    }

    @Override
    public UserDto getUser(String login) {
        User user = userRepository.getByLogin(login);
        if(user == null)
        {
            throw new NotFoundException("user not found");//TODO проверка на блок
        }

        Boolean blocked = false;

        try {
            CheckDto checkDto = new CheckDto(user.getId(), jwtService.getUser().getId());
            blocked = friendService.checkBlocking(checkDto, apiKeyProvider.getKey());
        } catch (Exception e) {
            throw new ExternalServiceErrorException("friend service error");
        }

        if(blocked) {
            throw new ForbiddenException("blocked");
        }
        return userMapper.map(user);
    }

    private String toParam(String str) {
        return str.toLowerCase();
    }
}
