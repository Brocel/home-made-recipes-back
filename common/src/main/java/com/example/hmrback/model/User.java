package com.example.hmrback.model;

import java.util.Set;

public record User(
        String id,

        String firstName,

        String lastName,

        String username,

        String email,

        String birthDate,

        String inscriptionDate,

        String avatar,

        Set<Role> roles
) {
}
