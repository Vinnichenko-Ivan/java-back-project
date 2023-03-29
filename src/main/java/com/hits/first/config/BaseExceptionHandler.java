package com.hits.first.config;

import com.hits.first.exception.CustomException;
import com.hits.first.exception.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    private ResponseEntity<MessageDto> handleIllegalArgument(CustomException ex) throws IOException {
        MessageDto messageDto = new MessageDto(ex.getMessage());
        return new ResponseEntity<MessageDto>(messageDto, ex.getStatus());
    }
}
