package com.hits.user.mapper;

import com.hits.common.dto.user.FullNameDto;
import com.hits.common.dto.user.NameSyncDto;
import com.hits.user.dto.UserDto;
import com.hits.user.dto.UserEditDto;
import com.hits.user.dto.UserRegisterDto;
import com.hits.user.model.User;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void mapUserRegisterTest() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        FullNameDto fullNameDto = new FullNameDto();
        fullNameDto.setName("name");
        fullNameDto.setSurname("sur");
        fullNameDto.setPatronymic("pat");
        userRegisterDto.setPhone("88005553535");
        userRegisterDto.setPassword("123@Qwert1");
        userRegisterDto.setCity("Town");
        userRegisterDto.setLogin("login");
        userRegisterDto.setEmail("fix3132@fixer.com");
        userRegisterDto.setFullName(fullNameDto);
        userRegisterDto.setAvatarId(UUID.randomUUID());
        userRegisterDto.setBirthDate(new Date(101010));

        User user = userMapper.map(userRegisterDto);

        assertEquals(userRegisterDto.getAvatarId(),user.getAvatarId());
        assertNull(user.getPassword());
        assertEquals(userRegisterDto.getCity(),user.getCity());
        assertEquals(userRegisterDto.getLogin(),user.getLogin());
        assertEquals(userRegisterDto.getEmail(),user.getEmail());
        assertEquals(userRegisterDto.getBirthDate(),user.getBirthDate());
        assertEquals(userRegisterDto.getFullName().getSurname(),user.getSurname());
        assertEquals(userRegisterDto.getFullName().getName(),user.getName());
        assertEquals(userRegisterDto.getFullName().getPatronymic(),user.getPatronymic());
    }

    @Test
    void mapUserEditTest() {
        UserEditDto userEditDto = new UserEditDto();
        FullNameDto fullNameDto = new FullNameDto();
        fullNameDto.setName("name");
        fullNameDto.setSurname("sur");
        fullNameDto.setPatronymic("pat");
        userEditDto.setPhone("88005553535");
        userEditDto.setCity("Town");
        userEditDto.setLogin("login");
        userEditDto.setEmail("fix3132@fixer.com");
        userEditDto.setFullName(fullNameDto);
        userEditDto.setAvatarId(UUID.randomUUID());
        userEditDto.setBirthDate(new Date(101010));
        User user = new User();
        userMapper.map(user, userEditDto);
        assertEquals(userEditDto.getAvatarId(),user.getAvatarId());
        assertNull(user.getPassword());
        assertEquals(userEditDto.getCity(),user.getCity());
        assertEquals(userEditDto.getLogin(),user.getLogin());
        assertEquals(userEditDto.getEmail(),user.getEmail());
        assertEquals(userEditDto.getBirthDate(),user.getBirthDate());
        assertEquals(userEditDto.getFullName().getSurname(),user.getSurname());
        assertEquals(userEditDto.getFullName().getName(),user.getName());
        assertEquals(userEditDto.getFullName().getPatronymic(),user.getPatronymic());
    }

    @Test
    void mapFromUserTest() {
        User user = genUser();
        UserDto userDto = userMapper.map(user);
        assertEquals(userDto.getBirthDate(),user.getBirthDate());
        assertEquals(userDto.getLogin(),user.getLogin());
        assertEquals(userDto.getId(),user.getId());
        assertEquals(userDto.getFullName().getName(),user.getName());
        assertEquals(userDto.getFullName().getSurname(),user.getSurname());
        assertEquals(userDto.getFullName().getPatronymic(),user.getPatronymic());

    }
    @Test
    void mapToSyncTest() {
        User user = genUser();
        NameSyncDto nameSyncDto = userMapper.mapToSync(user);
        assertEquals(nameSyncDto.getId(),user.getId());
        assertEquals(nameSyncDto.getFullName().getName(),user.getName());
        assertEquals(nameSyncDto.getFullName().getSurname(),user.getSurname());
        assertEquals(nameSyncDto.getFullName().getPatronymic(),user.getPatronymic());
    }
    @Test
    void mapToNameTest() {
        User user = genUser();
        FullNameDto fullNameDto = userMapper.mapToName(user);
        assertEquals(fullNameDto.getName(),user.getName());
        assertEquals(fullNameDto.getSurname(),user.getSurname());
        assertEquals(fullNameDto.getPatronymic(),user.getPatronymic());
    }

    private User genUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("name");
        user.setSurname("sur");
        user.setPatronymic("pat");
        user.setPassword("123@Qwert1");
        user.setCity("Town");
        user.setLogin("login");
        user.setEmail("fix3132@fixer.com");
        user.setPhone("88005553535");
        user.setAvatarId(UUID.randomUUID());
        user.setBirthDate(new Date(101010));
        return user;
    }
}
