package com.hits.user.service.impl;

import com.hits.user.service.PasswordService;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Override
    public String toHash(String password) {
        return password;
    }

    @Override
    public Boolean comparison(String password, String hash) {
        return hash.equals(toHash(password));
    }
}
