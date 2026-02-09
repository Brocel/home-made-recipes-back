package com.example.hmrback.config;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
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
    public static final String LOGIN = BASE_API + "/login";
    /**
     * Origines autorisées pour le front (séparées par des virgules). * Exemple .env : FRONTEND_URLS=http://localhost:4200,https://app.mondomaine.com
     */
    @Value("${FRONTEND_URLS:http://localhost:4200}")
    private String frontendUrlsRaw;
    private static final String[] SWAGGER_WHITELIST = {"/v3/api-docs/**",
                                                       "/swagger-ui/**",
                                                       "/swagger-ui.html",
                                                       "/swagger-resources/**",
                                                       "/webjars/**"};
    private static final String[] PUBLIC_WHITELIST = {LOGIN,
                                                      BASE_API + "/register",
                                                      "/css/**",
                                                      "/js/**",
                                                      "/images/**",
                                                      "/actuator/health",
                                                      "/actuator/info",
                                                      BASE_API + "/auth/google"};
    private static final String[] PUBLIC_RECIPE_WHITELIST = {BASE_API + "/recipes/daily"};

    @Bean
    public JwtDecoder googleJwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                               .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Build request matcher to ignore CSRF for the initial Google auth POST
        RequestMatcher ignoreCsrfForAuthGoogle = (HttpServletRequest request) -> "/hmr/api/auth/google".equals(request.getRequestURI()) && HttpMethod.POST.matches(request.getMethod());
        http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                              .ignoringRequestMatchers(ignoreCsrfForAuthGoogle))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authorizeHttpRequests(auth -> auth.requestMatchers(SWAGGER_WHITELIST)
                                               .permitAll()
                                               .requestMatchers(PUBLIC_WHITELIST)
                                               .permitAll()
                                               .requestMatchers(PUBLIC_RECIPE_WHITELIST)
                                               .permitAll()
                                               .anyRequest()
                                               .authenticated())
            .oauth2Login(AbstractHttpConfigurer::disable)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(googleJwtDecoder())))
            .logout(logout -> logout.logoutUrl("/logout")
                                    .logoutSuccessUrl("/login?logout")
                                    .invalidateHttpSession(true)
                                    .deleteCookies("JSESSIONID"));
        return http.build();
    }

    /**
     * CorsConfigurationSource configurable via la variable FRONTEND_URLS.
     * - allowCredentials true pour permettre l'envoi de cookies de session (si tu utilises session côté serveur)
     * - autorise méthodes courantes et headers
     */
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
