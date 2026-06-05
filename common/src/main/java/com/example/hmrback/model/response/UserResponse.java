package com.example.hmrback.model.response;

import com.example.hmrback.constant.DtoContants;
import com.example.hmrback.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record UserResponse(
        String id,

        @JsonProperty(DtoContants.FIRST_NAME)
        String firstName,

        @JsonProperty(DtoContants.LAST_NAME)
        String lastName,

        String username,

        String email,

        @JsonProperty(DtoContants.BIRTH_DATE)
        String birthDate,

        @JsonProperty(DtoContants.INSCRIPTION_DATE)
        String inscriptionDate,

        @JsonProperty(DtoContants.AVATAR)
        String avatarName,

        Set<Role> roles
) {}