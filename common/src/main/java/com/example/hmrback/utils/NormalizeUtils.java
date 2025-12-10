package com.example.hmrback.utils;

import jakarta.validation.constraints.NotNull;

public class NormalizeUtils {

    private NormalizeUtils() {
    }

    public static String normalize(@NotNull String value) {
        return value.trim().toLowerCase();
    }
}
