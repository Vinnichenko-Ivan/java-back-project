package com.hits.user.exception;

import org.springframework.http.HttpStatus;

public class BadCredentialsException extends CustomException {

    public BadCredentialsException(String message, HttpStatus status) {
        super(message, status);
    }

    public BadCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public BadCredentialsException() {
        super("wrong login or password", HttpStatus.UNAUTHORIZED);
    }
}
