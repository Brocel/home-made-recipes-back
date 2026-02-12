package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class RecipeControllerUpdateTest extends RecipeBaseIntegrationTest {

    private static String updateRecipeRequest;

    @BeforeAll
    static void setup() throws JsonProcessingException {
        // Request setup
        updateRecipeRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRecipe(1L,
                                                                                     false));
    }

    @Test
    @Order(1)
    @Transactional
    void updateRecipe_AsAdmin_ShouldSucceed() throws Exception {

        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                                .header("Authorization",
                                        "Bearer " + adminToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRecipeRequest))
               .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @Transactional
    void updateRecipe_AsAuthor_ShouldSucceed() throws Exception {

        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                                .header("Authorization",
                                        "Bearer " + userToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRecipeRequest))
               .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @Transactional
    void updateRecipe_AsOtherUser_ShouldFail() throws Exception {
        mockMvc.perform(put("/hmr/api/recipes/" + 1L)
                                .header("Authorization",
                                        "Bearer " + otherUserToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRecipeRequest))
               .andExpect(status().isForbidden());
    }
}
