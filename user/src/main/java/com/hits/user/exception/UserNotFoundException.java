package com.hits.user.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException() {
        super("user not found", HttpStatus.NOT_FOUND);
    }
}
