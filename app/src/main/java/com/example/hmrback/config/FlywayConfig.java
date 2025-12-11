package com.example.hmrback.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
public class FlywayConfig {

    @Bean
    public Flyway flywayConfiguration(DataSource dataSource, Environment env) {

        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .load();

        boolean enabled = Boolean.parseBoolean(env.getProperty("spring.flyway.enabled", "true"));

        if (enabled) {
            flyway.migrate();
        }

        return flyway;
    }
}
