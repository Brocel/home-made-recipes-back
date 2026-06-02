package com.example.hmrback.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for CORS (Cross-Origin Resource Sharing).
 * Maps to {@code app.cors} configuration in YAML files.
 * Provides type-safe access to CORS configuration.
 *
 * Example configuration:
 * <pre>
 * app:
 *   cors:
 *     frontend-urls: http://localhost:4200,http://localhost:3000
 *     allowed-methods:
 *       - GET
 *       - POST
 *       - PUT
 *       - DELETE
 *       - OPTIONS
 *     allowed-headers:
 *       - Content-Type
 *       - Authorization
 *       - Accept
 *       - X-Requested-With
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    private String frontendUrls;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;

    public String getFrontendUrls() {
        return frontendUrls;
    }

    public void setFrontendUrls(String frontendUrls) {
        this.frontendUrls = frontendUrls;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }
}

