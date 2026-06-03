package com.example.hmrback.model.error;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String errorKey,
        String message,
        String[] messageArgs,
        String path) {
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiError apiError = (ApiError) o;
        return timestamp == apiError.timestamp &&
                status == apiError.status &&
                error.equals(apiError.error) &&
                errorKey.equals(apiError.errorKey) &&
                message.equals(apiError.message) &&
                Arrays.equals(messageArgs, apiError.messageArgs) &&
                path.equals(apiError.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, error, errorKey, message, path) + Arrays.hashCode(messageArgs);
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", errorKey='" + errorKey + '\'' +
                ", message='" + message + '\'' +
                ", messageArgs=" + Arrays.toString(messageArgs) +
                ", path='" + path + '\'' +
                '}';
    }
}
