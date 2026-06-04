package com.example.hmrback.model.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateStepRequest(
        @Nullable Long id,

        @NotBlank
        String description,

        @NotNull
        @Positive
        Integer order) {
}
