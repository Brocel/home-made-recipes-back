package com.example.hmrback.persistence.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Base class for all repository tests.
 * Provides common test configuration and annotations to ensure consistency
 * across all persistence layer tests.
 * <p>
 * Features:
 * - @DataJpaTest for focused JPA testing with H2 database
 * - Auto-discovers PersistenceTestApplication as Spring Boot configuration
 * - @ActiveProfiles("test") to use test-specific Spring profile
 * - @TestMethodOrder for ordered test execution
 * </p>
 */
@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseRepositoryTU {
    // Base class for repository tests - all common configuration is handled here
}

