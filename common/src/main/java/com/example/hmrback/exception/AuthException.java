package com.example.hmrback.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class AuthException extends HomeMadeRecipeGenericException {
    public AuthException(HttpStatus status, LogLevel logLevel, String message) {
        super(status, logLevel, message);
    }
}
