package com.example.hmrback.config;

import com.example.hmrback.api.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String BASE_API = "/hmr/api";

    @Value("${FRONTEND_URLS:http://localhost:4200}")
    private String frontendUrlsRaw;

    private final JwtFilter jwtFilter;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    private static final String[] PUBLIC_WHITELIST = {
            BASE_API + "/auth/google",
            // login Google
            BASE_API + "/register",
            // registration Google
            "/css/**",
            "/js/**",
            "/images/**",
            "/actuator/health",
            "/actuator/info"
    };

    private static final String[] PUBLIC_RECIPE_WHITELIST = {
            BASE_API + "/recipes/daily"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF must be disabled for Google login POST
        RequestMatcher ignoreCsrfForAuthGoogle =
                request -> ("/hmr/api/auth/google".equals(request.getRequestURI())
                        && HttpMethod.POST.matches(request.getMethod()));

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(ignoreCsrfForAuthGoogle)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_WHITELIST)
                        .permitAll()
                        .requestMatchers(PUBLIC_WHITELIST)
                        .permitAll()
                        .requestMatchers(PUBLIC_RECIPE_WHITELIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login(AbstractHttpConfigurer::disable) // you don't use Spring OAuth2 login
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        // Add your JWT filter BEFORE UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter,
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedOrigins = parseFrontendUrls(frontendUrlsRaw);
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET",
                                                      "POST",
                                                      "PUT",
                                                      "DELETE",
                                                      "OPTIONS",
                                                      "PATCH"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization",
                                                      "Content-Type",
                                                      "X-CSRF-TOKEN"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",
                                         configuration);
        return source;
    }

    private List<String> parseFrontendUrls(String raw) {
        return Arrays.stream(raw.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .toList();
    }
}

