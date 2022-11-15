package com.marcin.springboot_react.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InsufficientUserLevelException extends Exception {

    public InsufficientUserLevelException(String message, Throwable reason) {
        super(message, reason);
    }

    public InsufficientUserLevelException(String message) {
        super(message);
    }
}
