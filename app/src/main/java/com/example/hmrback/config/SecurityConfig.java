package com.example.hmrback.config;

import com.example.hmrback.persistence.repository.UserRepository;
import com.example.hmrback.service.auth.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repo) {
        return new CustomUserDetailsService(repo);
    }

    // 1) Authorization Server security chain (higher priority)
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher endpointsMatcher = (HttpServletRequest request) -> {
            String path = request.getServletPath();
            if (path == null) return false;
            return path.startsWith("/oauth2/") ||
                   path.startsWith("/.well-known/") ||
                   path.equals("/oauth2/jwks") ||
                   path.equals("/.well-known/jwks.json") ||
                   path.equals("/login") ||
                   path.startsWith("/login/") ||
                   path.equals("/error");
        };

        http
            .securityMatcher(endpointsMatcher)
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
            .with(OAuth2AuthorizationServerConfigurer.authorizationServer(), Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    // 2) Resource Server / application security chain (lower priority)
    @Bean
    @Order(2)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
                // allow swagger and docs
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // allow your auth endpoints used by clients (e.g. login endpoint)
                .requestMatchers("/hmr/api/auth/**").permitAll()
                // everything else requires authentication
                .anyRequest().authenticated())
            // Resource server: validate incoming access tokens
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

}
