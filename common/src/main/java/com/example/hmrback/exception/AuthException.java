package com.example.hmrback.exception;

import com.example.hmrback.exception.util.ExceptionMessageEnum;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class AuthException extends HomeMadeRecipeGenericException {
    public AuthException(HttpStatus status, LogLevel logLevel, ExceptionMessageEnum message) {
        super(status, logLevel, message);
    }
}
