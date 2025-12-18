package com.example.hmrback.api.controller.auth;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.exception.AuthException;
import com.example.hmrback.model.request.RegisterRequest;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hmrback.exception.util.ExceptionMessageConstants.USER_EMAIL_ALREADY_EXISTS_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @Transactional
    void register_ShouldSucceed() throws Exception {

        String registerRequest = IntegrationTestUtils.toJson(CommonTestUtils.buildRegisterRequest(4L));

        mockMvc.perform(post("/hmr/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(registerRequest))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").exists());
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
}
