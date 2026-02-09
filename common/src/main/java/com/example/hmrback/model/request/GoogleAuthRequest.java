package com.example.hmrback.model.request;

import jakarta.validation.constraints.NotBlank;

import static com.example.hmrback.constant.ValidationConstants.IS_REQUIRED;

public record GoogleAuthRequest(
    @NotBlank(message = "Token Id" + IS_REQUIRED)
    String idToken) {
}
