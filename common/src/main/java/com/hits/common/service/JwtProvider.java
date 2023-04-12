package com.hits.common.service;

import com.hits.common.filter.JwtAuthentication;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.UUID;

@Service
@Slf4j
public class JwtProvider {

    private final SecretKey jwtAccessSecret;

    public JwtProvider(@Value("${jwt.secret.access}") String secret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }


    public Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public UUID getId() {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(authentication.getFirstName());
    }

    public String getLogin() {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getUsername();
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

}
