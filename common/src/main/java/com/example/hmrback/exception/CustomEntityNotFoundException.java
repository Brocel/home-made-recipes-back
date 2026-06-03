package com.example.hmrback.exception;

import com.example.hmrback.exception.util.ExceptionMessageEnum;
import jakarta.persistence.PersistenceException;
import lombok.Getter;

import static com.example.hmrback.exception.util.ExceptionUtils.toFormattedMessage;

@Getter
public class CustomEntityNotFoundException extends PersistenceException {

    private final ExceptionMessageEnum exceptionEnum;
    private final transient Object[] formatArgs;

    public CustomEntityNotFoundException(ExceptionMessageEnum exceptionEnum, Object... formatArgs) {
        super(toFormattedMessage(exceptionEnum.getMessage(), formatArgs));
        this.exceptionEnum = exceptionEnum;
        this.formatArgs = formatArgs;
    }
}
