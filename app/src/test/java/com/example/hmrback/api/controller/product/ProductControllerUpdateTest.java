package com.example.hmrback.api.controller.product;

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
class ProductControllerUpdateTest extends RecipeBaseIntegrationTest {

    private static String updateProductRequest;

    @BeforeAll
    static void setup() throws JsonProcessingException {
        // Request setup
        updateProductRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildProduct(1L));
    }

    @Test
    @Order(1)
    @Transactional
    void updateProduct_AsAdmin_ShouldSucceed() throws Exception {

        mockMvc.perform(put("/hmr/api/products/" + 1L)
                                .header("Authorization",
                                        "Bearer " + adminToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateProductRequest))
               .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @Transactional
    void updateProduct_AsUser_ShouldFail() throws Exception {
        mockMvc.perform(put("/hmr/api/products/" + 1L)
                                .header("Authorization",
                                        "Bearer " + userToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateProductRequest))
               .andExpect(status().isForbidden());
    }
}
