package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class RecipeControllerCreateTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @WithMockUser(username = "username1")
    @Transactional
    void createRecipe() throws Exception {
        String createRecipeRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildRecipeForCreation(1L));

        mockMvc.perform(post("/hmr/api/recipes")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRecipeRequest))
            .andExpect(status().isOk());
    }
}
