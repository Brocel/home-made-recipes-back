package com.example.hmrback.api.controller.product;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hmrback.utils.test.TestConstants.FAKE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ProductControllerCreateTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @Transactional
    void createProduct_withUser() throws Exception {
        boolean existingProduct = false;
        String createProductRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildProductForCreation(existingProduct));

        mockMvc.perform(post("/hmr/api/products")
                                .header("Authorization",
                                        "Bearer " + userToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createProductRequest))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.name").exists())
               .andExpect(jsonPath("$.name").value(FAKE))
               .andExpect(jsonPath("$.ingredient_type").exists())
               .andExpect(jsonPath("$.ingredient_type").value("OTHER"));
    }

    @Test
    @Order(2)
    @Transactional
    void createProduct_withAdmin() throws Exception {
        boolean existingProduct = false;
        String createProductRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildProductForCreation(existingProduct));

        mockMvc.perform(post("/hmr/api/products")
                                .header("Authorization",
                                        "Bearer " + adminToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createProductRequest))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.name").exists())
               .andExpect(jsonPath("$.name").value(FAKE))
               .andExpect(jsonPath("$.ingredient_type").exists())
               .andExpect(jsonPath("$.ingredient_type").value("OTHER"));
    }

    @Test
    @Order(3)
    @Transactional
    void createProduct_withExistingProduct() throws Exception {
        boolean existingProduct = true;
        String createProductRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildProductForCreation(existingProduct));

        mockMvc.perform(post("/hmr/api/products")
                                .header("Authorization",
                                        "Bearer " + userToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createProductRequest))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.name").exists())
               .andExpect(jsonPath("$.name").value("Carrot"))
               .andExpect(jsonPath("$.ingredient_type").exists())
               .andExpect(jsonPath("$.ingredient_type").value("VEGETABLE"));
    }
}
