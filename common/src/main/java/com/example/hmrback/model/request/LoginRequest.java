package com.example.hmrback.model.request;

import jakarta.validation.constraints.NotBlank;

import static com.example.hmrback.constant.ValidationConstants.IS_REQUIRED;

public record LoginRequest(
        @NotBlank(message = "Email" + IS_REQUIRED)
        String email,
        @NotBlank(message = "Password" + IS_REQUIRED)
        String password
) {
}
