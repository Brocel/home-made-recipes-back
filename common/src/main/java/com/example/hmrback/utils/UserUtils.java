package com.example.hmrback.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class UserUtils {

    private UserUtils() {
    }

    public static Set<String> getCommaSeparatedEmails(String raw) {
        return Arrays.stream(raw.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .map(String::toLowerCase)
                     .collect(Collectors.toSet());
    }

}
