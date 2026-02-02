package com.example.hmrback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class HomeMadeRecipesBackApplicationTest {

    @Test
    void contextLoads() {
        // If the application context loads, this test passes
    }
}
