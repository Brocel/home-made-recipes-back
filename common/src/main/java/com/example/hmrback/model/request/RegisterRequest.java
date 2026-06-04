package com.example.hmrback.model.request;

import com.example.hmrback.constant.DtoContants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import static com.example.hmrback.constant.ValidationConstants.IS_REQUIRED;

public record RegisterRequest(
        @NotBlank(message = "First Name" + IS_REQUIRED)
        @JsonProperty(DtoContants.FIRST_NAME)
        String firstName,
        @NotBlank(message = "Last Name" + IS_REQUIRED)
        @JsonProperty(DtoContants.LAST_NAME)
        String lastName,
        @NotBlank(message = "Username" + IS_REQUIRED)
        String username,
        @NotBlank(message = "Email" + IS_REQUIRED)
        String email,
        @NotBlank(message = "Password" + IS_REQUIRED)
        String password,
        @NotBlank(message = "Birth Date" + IS_REQUIRED)
        @JsonProperty(DtoContants.BIRTH_DATE)
        String birthDate
) {
}
