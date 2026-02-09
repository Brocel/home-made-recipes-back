package com.example.hmrback.model.request;

public record RegistrationRequest(
        String email,
        String firstName,
        String lastName,
        String username,
        String birthDate
) {
}
