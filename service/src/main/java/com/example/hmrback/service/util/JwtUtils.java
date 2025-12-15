package com.example.hmrback.service.util;

import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private JwtUtils() {
    }

    public static Map<String, Object> buildClaims(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles().stream().map(RoleEntity::getName).toList());
        claims.put("userId", user.getId());
        return claims;

    }
}
