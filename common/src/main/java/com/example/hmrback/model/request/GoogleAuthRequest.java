package com.example.hmrback.model.request;

import com.example.hmrback.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import static com.example.hmrback.constant.ValidationConstants.IS_REQUIRED;

public record GoogleAuthRequest(
    @NotBlank(message = "Token Id" + IS_REQUIRED)
    String idToken) {
}
