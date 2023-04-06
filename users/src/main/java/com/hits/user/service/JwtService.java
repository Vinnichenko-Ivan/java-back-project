package com.hits.user.service;

import com.hits.user.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;

public interface JwtService {
    public String generateAccessToken(@NonNull User user);

    boolean validateAccessToken(@NonNull String accessToken);

    Claims getAccessClaims(@NonNull String token);

    User getUser();

    String getLogin();
}
