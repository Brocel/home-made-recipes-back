package com.example.hmrback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal Spring Boot application for persistence layer testing.
 * Used only by @DataJpaTest to provide Spring context discovery.
 * This is a best practice for testing library modules that don't have their own main application.
 *
 * @DataJpaTest will auto-discover this configuration and use it for persistence layer tests.
 */
@SpringBootApplication
public class PersistenceTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersistenceTestApplication.class, args);
    }
}

