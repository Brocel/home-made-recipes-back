package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class RecipeControllerUpdateTest extends RecipeBaseIntegrationTest {

    private static String updateRecipeRequest;

    @BeforeAll
    static void setup() throws JsonProcessingException {
        // Request setup
        updateRecipeRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRecipe(1L, false));
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @Transactional
    void updateRecipe_AsAdmin_ShouldSucceed() throws Exception {
        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRecipeRequest))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "username1")
    @Transactional
    void updateRecipe_AsAuthor_ShouldSucceed() throws Exception {
        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRecipeRequest))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "otherUser")
    @Transactional
    void updateRecipe_AsOtherUser_ShouldFail() throws Exception {
        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + otherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRecipeRequest))
            .andExpect(status().isForbidden());
    }
}
