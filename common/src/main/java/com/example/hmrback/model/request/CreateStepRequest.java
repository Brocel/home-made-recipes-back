package com.example.hmrback.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateStepRequest(

        @NotBlank
        String description,

        @NotNull
        @Positive
        Integer order) {
}
