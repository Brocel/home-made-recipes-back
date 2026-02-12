package com.example.hmrback.utils;

import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class JwtUtils {

    private JwtUtils() {
    }

    private static Key getKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(UserEntity user,
                                       Long expirationMinutes,
                                       String secretKey) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationMinutes * 60);

        return Jwts.builder()
                   .subject(user.getEmail())
                   .claim("uid",
                          user.getId())
                   .claim("username",
                          user.getUsername())
                   .claim("roles",
                          user.getRoles()
                              .stream()
                              .map(RoleEntity::getName)
                              .toList())
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(exp))
                   .signWith(getKey(secretKey))
                   .compact();
    }

    public static Claims parse(String token,
                               String secretKey) {
        return Jwts.parser()
                   .verifyWith((SecretKey) getKey(secretKey))
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

}
