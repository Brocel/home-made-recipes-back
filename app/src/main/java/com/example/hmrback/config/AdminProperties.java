package com.example.hmrback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AdminProperties {

    private final Set<String> adminEmails;

    public AdminProperties(
        @Value("${admin.emails}")
        String adminEmailsRaw) {
        this.adminEmails = Arrays.stream(adminEmailsRaw.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

    public Set<String> getAdminEmails() {
        return adminEmails;
    }
}
