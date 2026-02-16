package com.example.hmrback.exception;

import com.example.hmrback.exception.util.ExceptionMessageEnum;
import jakarta.persistence.PersistenceException;
import lombok.Getter;

@Getter
public class CustomEntityNotFoundException extends PersistenceException {

    private final ExceptionMessageEnum exceptionEnum;

    public CustomEntityNotFoundException(ExceptionMessageEnum exceptionEnum, String message) {
        super(message);
        this.exceptionEnum = exceptionEnum;
    }
}
