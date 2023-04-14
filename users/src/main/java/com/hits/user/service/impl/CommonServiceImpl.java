package com.hits.user.service.impl;

import com.hits.user.repository.UserRepository;
import com.hits.user.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final UserRepository userRepository;

    @Override
    public Boolean checkUser(UUID id) {
        return userRepository.existsById(id);
    }
}
