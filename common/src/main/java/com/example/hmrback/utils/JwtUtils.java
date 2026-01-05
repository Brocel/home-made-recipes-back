package com.example.hmrback.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public class JwtUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JwtUtils.class);

    private JwtUtils() {
    }

    public static void validateJwtClaims(Jwt jwt, String googleClientId) {
        // aud check
        List<String> aud = jwt.getAudience();
        if (googleClientId == null || googleClientId.isBlank()) {
            LOG.warn("GOOGLE_CLIENT_ID not configured; skipping aud check");
        } else if (aud == null || !aud.contains(googleClientId)) {
            throw new IllegalStateException("Invalid audience in ID token");
        }

        // iss check
        String iss = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
        if (!"https://accounts.google.com".equals(iss) && !"accounts.google.com".equals(iss)) {
            throw new IllegalStateException("Invalid issuer in ID token");
        }

        // email_verified
        Boolean emailVerified = jwt.getClaimAsBoolean("email_verified");
        if (emailVerified == null || !emailVerified) {
            throw new IllegalStateException("Email not verified by Google");
        }
    }

    public static String extractName(Jwt jwt) {
        String name = jwt.getClaimAsString("name");
        if (name != null && !name.isBlank()) return name;
        String given = jwt.getClaimAsString("given_name");
        String family = jwt.getClaimAsString("family_name");
        if (given != null && family != null) return given + " " + family;
        return null;
    }

}
