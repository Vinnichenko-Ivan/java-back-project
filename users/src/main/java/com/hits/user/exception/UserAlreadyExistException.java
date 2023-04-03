package com.hits.user.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends CustomException {

    public UserAlreadyExistException(String message, HttpStatus status) {
        super(message, status);
    }

    public UserAlreadyExistException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public UserAlreadyExistException() {
        super("user already exist", HttpStatus.BAD_REQUEST);
    }
}
