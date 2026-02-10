package com.example.hmrback.service.security;

import com.example.hmrback.persistence.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;


    private Key getKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserEntity user) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofHours(6));

        return Jwts.builder()
                   .subject(user.getEmail())
                   .claim("id",
                          user.getId())
                   .claim("username",
                          user.getUsername())
                   .claim("roles",
                          user.getRoles())
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(exp))
                   .signWith(getKey(secretKey))
                   .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()
                   .verifyWith((SecretKey) getKey(secretKey))
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
}

