package com.example.hmrback.api.controller.product;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ProductControllerDeleteTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @Transactional
    void deleteProduct_AsAdmin_ShouldSucceed() throws Exception {

        mockMvc.perform(delete("/hmr/api/products/" + 1L)
                                .header("Authorization",
                                        "Bearer " + adminToken)
                                .with(csrf())
               )
               .andExpect(status().isNoContent());
    }

    @Test
    @Order(2)
    @Transactional
    void deleteProduct_AsUser_ShouldFail() throws Exception {
        mockMvc.perform(delete("/hmr/api/products/" + 1L)
                                .header("Authorization",
                                        "Bearer " + userToken)
                                .with(csrf())
               )
               .andExpect(status().isForbidden());
    }
}
