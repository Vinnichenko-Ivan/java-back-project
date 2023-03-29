package com.hits.first.exception;

import org.springframework.http.HttpStatus;

public class ProfileNotFoundException extends CustomException {

    public ProfileNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }

    public ProfileNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ProfileNotFoundException() {
        super("profile not found", HttpStatus.NOT_FOUND);
    }
}
