package com.example.hmrback.model.response;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public record AuthResult(
    String username,
    String displayName,
    Object principal,
    Set<SimpleGrantedAuthority> authorities) {
}
