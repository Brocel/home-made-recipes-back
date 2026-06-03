package com.example.hmrback.exception;

import com.example.hmrback.exception.util.ExceptionMessageEnum;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class AuthException extends HomeMadeRecipeGenericException {
    public AuthException(ExceptionMessageEnum exceptionMessageEnum, HttpStatus status, LogLevel logLevel, Object... args) {
        super(exceptionMessageEnum, null, status, logLevel, args);
    }
}
