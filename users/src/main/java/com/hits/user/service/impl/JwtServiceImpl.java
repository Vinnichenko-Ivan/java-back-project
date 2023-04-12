package com.hits.user.service.impl;


import com.hits.common.filter.JwtAuthentication;
import com.hits.common.service.JwtProvider;
import com.hits.user.model.User;
import com.hits.user.repository.UserRepository;
import com.hits.user.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j //https://struchkov.dev/blog/ru/jwt-implementation-in-spring/ - ТОП СТАТЬЯ
@Service
public class JwtServiceImpl implements JwtService {
    private final SecretKey jwtAccessSecret;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;
    public JwtServiceImpl(@Value("${jwt.secret.access}") String secret, JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    public String generateAccessToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("id", user.getId())
                .compact();
    }

    @Override
    public User getUser() {
        return userRepository.getByLogin(jwtProvider.getLogin());
    }

}
