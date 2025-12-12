package com.example.hmrback.api.controller.auth;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.exception.AuthException;
import com.example.hmrback.model.request.AuthRequest;
import com.example.hmrback.model.request.RegisterRequest;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hmrback.exception.util.ExceptionMessageConstants.BAD_CREDENTIAL_EXCEPTION_MESSAGE;
import static com.example.hmrback.exception.util.ExceptionMessageConstants.USER_EMAIL_ALREADY_EXISTS_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @Transactional
    void register_ShouldSucceed() throws Exception {

        String registerRequest = IntegrationTestUtils.toJson(CommonTestUtils.buildRegisterRequest(4L));

        mockMvc.perform(post("/hmr/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(registerRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Order(2)
    @Transactional
    void register_withAlreadyExistingUser_ShouldFail() throws Exception {

        RegisterRequest request = CommonTestUtils.buildRegisterRequest(1L);
        String registerRequest = IntegrationTestUtils.toJson(request);

        mockMvc.perform(post("/hmr/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
            .andExpect(result -> {
                Throwable resolved = result.getResolvedException();
                assertNotNull(resolved);
                assertInstanceOf(AuthException.class, resolved);
                assertEquals(USER_EMAIL_ALREADY_EXISTS_MESSAGE.formatted(request.user().email()), resolved.getMessage());
            })
            .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    @Transactional
    void login_shouldSucceed() throws Exception {

        AuthRequest request = CommonTestUtils.buildAuthRequest(1L, false);
        String registerRequest = IntegrationTestUtils.toJson(request);

        mockMvc.perform(post("/hmr/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @Transactional
    void login_withWrongCredential_shouldFail() throws Exception {

        AuthRequest request = CommonTestUtils.buildAuthRequest(1L, true);
        String registerRequest = IntegrationTestUtils.toJson(request);

        mockMvc.perform(post("/hmr/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
            .andExpect(result -> {
                Throwable resolved = result.getResolvedException();
                assertInstanceOf(AuthException.class, resolved);
                assertEquals(BAD_CREDENTIAL_EXCEPTION_MESSAGE, resolved.getMessage());
            })
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(5)
    @Transactional
    void login_withWrongUsername_shouldFail() throws Exception {

        AuthRequest request = CommonTestUtils.buildAuthRequest(10L, false);
        String registerRequest = IntegrationTestUtils.toJson(request);

        mockMvc.perform(post("/hmr/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
            .andExpect(result -> {
                Throwable resolved = result.getResolvedException();
                assertNotNull(resolved);
                assertInstanceOf(AuthException.class, resolved);
                assertEquals(BAD_CREDENTIAL_EXCEPTION_MESSAGE, resolved.getMessage());
            })
            .andExpect(status().isUnauthorized());
    }
}
