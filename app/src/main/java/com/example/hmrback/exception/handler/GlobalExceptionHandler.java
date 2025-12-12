package com.example.hmrback.exception.handler;

import com.example.hmrback.exception.HomeMadeRecipeGenericException;
import com.example.hmrback.exception.util.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.hmrback.exception.util.ExceptionMessageConstants.ACCESS_DENIED_EXCEPTION_MESSAGE;
import static com.example.hmrback.exception.util.ExceptionMessageConstants.EXCEPTION_BASE_MESSAGE;
import static com.example.hmrback.exception.util.ExceptionUtils.buildResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, ACCESS_DENIED_EXCEPTION_MESSAGE, request.getRequestURI());
    }

    @ExceptionHandler(HomeMadeRecipeGenericException.class)
    public ResponseEntity<ApiError> handleHomeMadeRecipeGenericException(HomeMadeRecipeGenericException ex, HttpServletRequest request) {

        if (LogLevel.WARN.equals(ex.getLogLevel())) {
            LOGGER.warn(ex.getMessage());
        } else {
            LOGGER.error(EXCEPTION_BASE_MESSAGE, request.getRequestURI(), ex);
        }

        return buildResponseEntity(ex.getStatus(), ex.getMessage(), request.getRequestURI());
    }

}
