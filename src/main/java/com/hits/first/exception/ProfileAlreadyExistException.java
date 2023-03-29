package com.hits.first.exception;

import org.springframework.http.HttpStatus;

public class ProfileAlreadyExistException extends CustomException {

    public ProfileAlreadyExistException(String message, HttpStatus status) {
        super(message, status);
    }

    public ProfileAlreadyExistException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public ProfileAlreadyExistException() {
        super("profile already exist", HttpStatus.BAD_REQUEST);
    }
}
