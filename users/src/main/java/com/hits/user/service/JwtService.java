package com.hits.user.service;

import com.hits.user.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;

public interface JwtService {
    String generateAccessToken(@NonNull User user);

    User getUser();

}
