package com.example.hmrback.exception.handler;

import com.example.hmrback.exception.CustomEntityNotFoundException;
import com.example.hmrback.exception.HomeMadeRecipeGenericException;
import com.example.hmrback.model.error.ApiError;
import com.example.hmrback.exception.util.ExceptionMessageEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.hmrback.exception.util.ExceptionUtils.buildResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.FORBIDDEN,
                ExceptionMessageEnum.ACCESS_DENIED,
                request.getRequestURI());
    }

    @ExceptionHandler(CustomEntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(CustomEntityNotFoundException ex,
                                                         HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND,
                ex.getExceptionEnum(),
                request.getRequestURI(),
                ex.getFormatArgs());
    }

    @ExceptionHandler(HomeMadeRecipeGenericException.class)
    public ResponseEntity<ApiError> handleHomeMadeRecipeGenericException(HomeMadeRecipeGenericException ex,
                                                                         HttpServletRequest request) {
        return buildResponseEntity(ex.getStatus(),
                ex.getExceptionEnum(),
                request.getRequestURI(),
                ex.getFormatArgs());
    }

}
