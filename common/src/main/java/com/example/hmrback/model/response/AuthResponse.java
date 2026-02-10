package com.example.hmrback.model.response;

import com.example.hmrback.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
        String token,
        User user
) {}

