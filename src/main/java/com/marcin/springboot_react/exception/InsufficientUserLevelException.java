package com.marcin.springboot_react.exception;

public class InsufficientUserLevelException extends Exception {

    public InsufficientUserLevelException(String message, Throwable reason) {
        super(message, reason);
    }

    public InsufficientUserLevelException(String message) {
        super(message);
    }
}
