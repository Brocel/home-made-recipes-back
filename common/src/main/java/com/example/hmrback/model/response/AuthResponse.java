package com.example.hmrback.model.response;

import com.example.hmrback.model.User;

public record AuthResponse(
        String token,
        User user
) {}

