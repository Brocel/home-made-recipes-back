package com.example.hmrback.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Flyway locations.
 * Maps to {@code app.flyway.locations} configuration in YAML files.
 * Provides type-safe access to flyway location configuration.
 *
 * Example configuration:
 * <pre>
 * app:
 *   flyway:
 *     locations:
 *       migration: classpath:db/migration
 *       local: classpath:db/local
 * </pre>
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.flyway.locations")
public class FlywayProperties {

    private String migration;
    private String local;

}

