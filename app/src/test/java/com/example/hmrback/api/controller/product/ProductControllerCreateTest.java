package com.example.hmrback.api.controller.product;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hmrback.utils.test.TestConstants.FAKE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ProductControllerCreateTest extends RecipeBaseIntegrationTest {

    @Test
    @Order(1)
    @Transactional
    void createProduct_withUser() throws Exception {
        // TODO: generalize it for all tests
        UserDetails userDetails = User.withUsername("username1")
            .password("ignored")
            .authorities(new SimpleGrantedAuthority("ROLE_USER"))
            .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        boolean existingProduct = false;
        String createProductRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildProductForCreation(existingProduct));

        mockMvc.perform(post("/hmr/api/products")
                .with(authentication(auth))
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
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @Transactional
    void createProduct_withAdmin() throws Exception {
        boolean existingProduct = false;
        String createProductRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildProductForCreation(existingProduct));

        mockMvc.perform(post("/hmr/api/products")
                .header("Authorization", "Bearer " + adminToken)
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
    @WithMockUser(username = "username1")
    @Transactional
    void createProduct_withExistingProduct() throws Exception {
        boolean existingProduct = true;
        String createProductRequest = IntegrationTestUtils.toJson(ModelTestUtils.buildProductForCreation(existingProduct));

        mockMvc.perform(post("/hmr/api/products")
                .header("Authorization", "Bearer " + userToken)
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
