package com.example.hmrback.exception;

import com.example.hmrback.exception.util.ExceptionMessageEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class HomeMadeRecipeGenericException extends Exception {

    private final HttpStatus status;
    private final LogLevel logLevel;
    private final ExceptionMessageEnum exceptionEnum;

    public HomeMadeRecipeGenericException(ExceptionMessageEnum exceptionEnum,
                                          Throwable cause,
                                          HttpStatus status,
                                          LogLevel logLevel) {
        super(exceptionEnum.getMessage(),
              cause);
        this.status = status != null ?
                      status :
                      HttpStatus.INTERNAL_SERVER_ERROR;
        this.logLevel = logLevel != null ?
                        logLevel :
                        LogLevel.ERROR;
        this.exceptionEnum = exceptionEnum;
    }
}
