package com.example.hmrback.api.security;

import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.service.security.JwtService;
import com.example.hmrback.service.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtFilter(JwtService jwtService,
                     UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. Endpoints publics → on ignore
        if (path.startsWith("/hmr/api/auth/") ||
                path.equals("/hmr/api/register") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/hmr/api/recipes/daily")) {

            filterChain.doFilter(request,
                                 response);
            return;
        }

        // 2. Récupération du header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,
                                 response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 3. Vérification du JWT interne
            Claims claims = jwtService.parse(token);
            String email = claims.getSubject();

            if (email != null && SecurityContextHolder.getContext()
                                                      .getAuthentication() == null) {

                UserEntity user = userService.findByEmail(email)
                                             .orElse(null);

                if (user != null) {
                    List<SimpleGrantedAuthority> roles = user.getRoles()
                                                             .stream()
                                                             .map(role ->
                                                                          new SimpleGrantedAuthority(role.getName()
                                                                                                         .name()))
                                                             .toList();
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,
                                                                                                       null,
                                                                                                       roles);

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext()
                                         .setAuthentication(auth);
                }
            }

        } catch (Exception e) {
            // Token invalide → on ignore, pas de crash
        }

        filterChain.doFilter(request,
                             response);
    }
}
