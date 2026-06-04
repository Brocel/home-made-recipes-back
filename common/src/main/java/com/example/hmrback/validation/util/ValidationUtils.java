package com.example.hmrback.validation.util;

import com.example.hmrback.exception.util.ExceptionUtils;
import com.example.hmrback.model.error.ApiError;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;

public class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * Maps a field name and constraint type to the appropriate ValidationMessageEnum.     * Format: {FIELD_NAME}_{CONSTRAINT_TYPE}     * Example: "title" + "NotBlank" → "RECIPE_TITLE_REQUIRED"
     */
    public static ValidationMessageEnum mapFieldErrorToValidationEnum(String fieldName, String constraintType) {
        // First, try exact match: fieldName_ConstraintType (e.g., TITLE_NOTBLANK)
        String mappingKey = fieldName.toUpperCase() + "_" + (constraintType != null ? constraintType.toUpperCase() : "REQUIRED");

        try {
            return ValidationMessageEnum.valueOf(mappingKey);
        } catch (IllegalArgumentException e) {
            // Fallback: try with just field_REQUIRED
            String fallbackKey = fieldName.toUpperCase() + "_REQUIRED";
            try {
                return ValidationMessageEnum.valueOf(fallbackKey);
            } catch (IllegalArgumentException ex) {
                // Ultimate fallback - generic message
                return getDefaultValidationMessage(constraintType);
            }
        }
    }

    /**
     * Provides sensible defaults for unmapped constraint types.
     */
    public static ValidationMessageEnum getDefaultValidationMessage(String constraintType) {
        if ("NotBlank".equalsIgnoreCase(constraintType) || "NotNull".equalsIgnoreCase(constraintType)) {
            return ValidationMessageEnum.FIELD_REQUIRED;
        } else if ("Email".equalsIgnoreCase(constraintType)) {
            return ValidationMessageEnum.EMAIL_INVALID;
        } else if ("Positive".equalsIgnoreCase(constraintType)) {
            return ValidationMessageEnum.FIELD_POSITIVE;
        } else if ("ValidDate".equalsIgnoreCase(constraintType)) {
            return ValidationMessageEnum.DATE_INVALID;
        }  else {
            return ValidationMessageEnum.FIELD_INVALID;
        }
    }


    public static ResponseEntity<ApiError> buildResponseEntity(HttpStatus status,
                                                               ValidationMessageEnum validationMessageEnum,
                                                               String path,
                                                               Object... args) {
        return ResponseEntity.status(status)
                .body(toApiError(status, validationMessageEnum, path, args));
    }


    public static ApiError toApiError(HttpStatus status,
                                      ValidationMessageEnum validationMessageEnum,
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
                validationMessageEnum.name(),
                validationMessageEnum.getErrorKey(),
                ExceptionUtils.toFormattedMessage(validationMessageEnum.getMessage(), args),
                messageArgs,
                path);
    }
}
