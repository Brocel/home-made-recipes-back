package com.example.hmrback.model.request;

import jakarta.validation.constraints.NotBlank;

import static com.example.hmrback.constant.ValidationConstants.IS_REQUIRED;

public record AuthRequest(

    @NotBlank(message = "Username" + IS_REQUIRED)
    String username,

    @NotBlank(message = "Password" + IS_REQUIRED)
    String password) {
}
