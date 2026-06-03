package com.example.hmrback.exception.util;

import com.example.hmrback.model.error.ApiError;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;

public class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static ApiError toApiError(HttpStatus status,
                                      ExceptionMessageEnum exceptionMessage,
                                      String path,
                                      Object... args) {

        String[] messageArgs = null;
        if (!ArrayUtils.isEmpty(args)) {
            // Convert args into a list of strings
            messageArgs = Arrays.stream(args)
                    .map(String::valueOf)
                    .toArray(String[]::new);
        }

        return new ApiError(Instant.now(),
                status.value(),
                exceptionMessage.name(),
                exceptionMessage.getErrorKey(),
                toFormattedMessage(exceptionMessage.getMessage(), args),
                messageArgs,
                path);
    }

    public static ResponseEntity<ApiError> buildResponseEntity(HttpStatus status,
                                                               ExceptionMessageEnum exceptionMessageEnum,
                                                               String path,
                                                               Object... args) {
        return ResponseEntity.status(status)
                .body(toApiError(status, exceptionMessageEnum, path, args));
    }

    public static String toFormattedMessage(String message, Object... args) {
        if (!ArrayUtils.isEmpty(args)) {
            // Message formatting
            try {
                return message.formatted(args);
            } catch (Exception e) {
                return message;
            }
        }

        return message;
    }
}
