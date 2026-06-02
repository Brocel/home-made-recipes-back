package com.example.hmrback.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    @ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
    public Flyway flywayConfiguration(DataSource dataSource,
                                      Environment env,
                                      FlywayProperties flywayProperties) {
        String[] locations = resolveLocations(env);

        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations(locations)
            .load();

        boolean enabled = Boolean.parseBoolean(env.getProperty("spring.flyway.enabled", "true"));

        if (enabled) {
            flyway.migrate();
        }

        return flyway;
    }

    /**
     * Resolves migration locations based on active profile.
     * For 'local' profile, includes both migration and local locations.
     * For other profiles, includes migration locations only.
     */
    private String[] resolveLocations(Environment env) {
        if (env.acceptsProfiles(Profiles.of("local"))) {
            return new String[]{"classpath:db/migration", "classpath:db/local"};
        }
        return new String[]{"classpath:db/migration"};
    }
}
