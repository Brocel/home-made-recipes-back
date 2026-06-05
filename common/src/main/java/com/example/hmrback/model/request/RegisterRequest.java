package com.example.hmrback.model.request;

import com.example.hmrback.constant.DtoContants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        @JsonProperty(DtoContants.FIRST_NAME)
        String firstName,
        @NotBlank
        @JsonProperty(DtoContants.LAST_NAME)
        String lastName,
        @NotBlank
        String username,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        @JsonProperty(DtoContants.BIRTH_DATE)
        String birthDate
) {
}
