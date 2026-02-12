package com.example.hmrback.api.controller.auth;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class AuthControllerTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @Transactional
    void register() throws Exception {
        String registerRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRegisterRequest(true));

        mockMvc.perform(post("/hmr/api/auth/register")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerRequest))
               .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @Transactional
    void register_withInvalidRequest_shouldFail() throws Exception {
        String registerRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRegisterRequest(false));

        mockMvc.perform(post("/hmr/api/auth/register")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerRequest))
               .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(3)
    @Transactional
    void login() throws Exception {
        String loginRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildLoginRequest(true));
        String registerRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRegisterRequest(true));

        mockMvc.perform(post("/hmr/api/auth/register")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerRequest));

        mockMvc.perform(post("/hmr/api/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest))
               .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @Transactional
    void login_withInvalidPassword_shouldFail() throws Exception {
        String loginRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildLoginRequest(false));
        String registerRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRegisterRequest(true));

        mockMvc.perform(post("/hmr/api/auth/register")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerRequest));

        mockMvc.perform(post("/hmr/api/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest))
               .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(5)
    void checkUsername_exists() throws Exception {
        String existingUsername = savedUser.getUsername();

        mockMvc.perform(get("/hmr/api/auth/check-username")
                                .param("username",
                                       existingUsername)
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
    }

    @Test
    @Order(6)
    void checkUsername_notExists() throws Exception {
        mockMvc.perform(get("/hmr/api/auth/check-username")
                                .param("username",
                                       "unknown_user_123")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string("false"));
    }

}
