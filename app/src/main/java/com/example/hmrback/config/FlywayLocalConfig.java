package com.example.hmrback.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@Profile("local")
public class FlywayLocalConfig {

    @Bean
    public Flyway flywayConfiguration(DataSource dataSource, Environment env) {

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/local")
                .load();

        boolean enabled = Boolean.parseBoolean(env.getProperty("spring.flyway.enabled", "true"));

        if (enabled) {
            flyway.migrate();
        }

        return flyway;
    }
}
