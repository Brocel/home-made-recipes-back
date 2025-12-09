package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class RecipeControllerDeleteTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @WithMockUser(username = "username1")
    @Transactional
    void deleteRecipe_AsAuthor_ShouldSucceed() throws Exception {

        mockMvc.perform(delete("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isNoContent());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "otherUser")
    @Transactional
    void deleteRecipe_AsOtherUser_ShouldFail() throws Exception {
        mockMvc.perform(delete("/hmr/api/recipes/" + 1L)
                .header("Authorization", "Bearer " + otherToken))
            .andExpect(status().isForbidden());
    }

}
