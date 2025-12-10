package com.example.hmrback.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flywayConfiguration(DataSource dataSource) {
        return Flyway.configure()
            .dataSource(dataSource)
            .load();
    }
}
