package com.example.hmrback.model.request;

import com.example.hmrback.validation.ValidDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserUpdateRequest(
        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("username")
        String username,

        @JsonProperty("birth_date")
        @ValidDate
        String birthDate,

        @JsonProperty("avatar_name")
        String avatarName
) {
}
