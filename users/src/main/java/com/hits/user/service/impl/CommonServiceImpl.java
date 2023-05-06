package com.hits.user.service.impl;

import com.hits.common.dto.user.FullNameDto;
import com.hits.common.exception.NotFoundException;
import com.hits.user.mapper.UserMapper;
import com.hits.user.repository.UserRepository;
import com.hits.user.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public Boolean checkUser(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public FullNameDto getUserName(UUID id) {
        return userMapper.mapToName(userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found")));
    }
}
