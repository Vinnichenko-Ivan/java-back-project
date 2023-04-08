package com.hits.friends.exception;

import com.hits.user.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotImplementedException extends CustomException {

    public NotImplementedException(String message, HttpStatus status) {
        super(message, status);
    }

    public NotImplementedException(String message) {
        super(message, HttpStatus.NOT_IMPLEMENTED);
    }

    public NotImplementedException() {
        super("not implemented", HttpStatus.NOT_IMPLEMENTED);
    }
}
