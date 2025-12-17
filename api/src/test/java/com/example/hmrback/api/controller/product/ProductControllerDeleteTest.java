package com.example.hmrback.api.controller.product;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ProductControllerDeleteTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @Transactional
    void deleteRecipe_AsAdmin_ShouldSucceed() throws Exception {

        mockMvc.perform(delete("/hmr/api/products/" + 1L)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isNoContent());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "username1")
    @Transactional
    void deleteRecipe_AsUser_ShouldFail() throws Exception {
        mockMvc.perform(delete("/hmr/api/products/" + 1L)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }
}
