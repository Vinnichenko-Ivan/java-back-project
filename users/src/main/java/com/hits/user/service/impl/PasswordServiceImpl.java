package com.hits.user.service.impl;

import com.hits.user.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String toHash(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Boolean comparison(String password, String hash) {
        return passwordEncoder.matches(password, hash);
    }
}
