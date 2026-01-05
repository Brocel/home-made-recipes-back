package com.example.hmrback.service.auth;

import com.example.hmrback.model.response.AuthResult;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.service.user.UserService;
import com.example.hmrback.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleAuthService.class);

    private final JwtDecoder googleJwtDecoder;

    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Transactional
    public AuthResult processGoogleLogin(String idToken) {
        // 1) Decode and validate signature + exp via googleJwtDecoder
        Jwt jwt = googleJwtDecoder.decode(idToken);

        // 2) Validate claims: aud, iss, email_verified
        JwtUtils.validateJwtClaims(jwt, googleClientId);

        String email = jwt.getClaimAsString("email");
        String name = JwtUtils.extractName(jwt);
        String provider = "google";
        String providerId = jwt.getClaimAsString("sub");

        // 3) Find or create user and link oauth account
        UserEntity user = userService.findOrCreateAndLinkUser(provider, providerId, email, name);

        // 4) Build authorities from roles
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
            .map(r -> new SimpleGrantedAuthority(r.getName().name()))
            .collect(Collectors.toSet());

        // principal can be a lightweight object (username) or a custom UserDetails
        String principal = user.getUsername();

        return new AuthResult(user.getUsername(), user.getDisplayName(), principal, authorities);
    }
}

