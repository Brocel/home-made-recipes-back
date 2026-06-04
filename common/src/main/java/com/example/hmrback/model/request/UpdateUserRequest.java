package com.example.hmrback.model.request;

import com.example.hmrback.constant.DtoContants;
import com.example.hmrback.validation.ValidDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateUserRequest(
        @JsonProperty(DtoContants.FIRST_NAME)
        String firstName,

        @JsonProperty(DtoContants.LAST_NAME)
        String lastName,

        String username,

        @JsonProperty(DtoContants.BIRTH_DATE)
        @ValidDate
        String birthDate,

        @JsonProperty(DtoContants.AVATAR_NAME)
        String avatarName
) {
}
