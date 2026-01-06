package com.example.hmrback.api.controller.product;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.ProductFilterEnum;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ProductControllerReadTest extends RecipeBaseIntegrationTest {

    private final Map<ProductFilterEnum, Integer> expectedContentSizeResults = Map.of(
        ProductFilterEnum.NULL, 0,
        ProductFilterEnum.JUST_NAME, 1,
        ProductFilterEnum.ALL_TYPE, 17,
        ProductFilterEnum.ONLY_OTHER, 0,
        ProductFilterEnum.SINGLE_CATEGORY, 1,
        ProductFilterEnum.THREE_CATEGORIES, 3
    );

    @ParameterizedTest
    @EnumSource(ProductFilterEnum.class)
    @Order(1)
    @Transactional
    void searchProducts(ProductFilterEnum productFilterEnum) throws Exception {

        String productsFilter = IntegrationTestUtils.toJson(CommonTestUtils.buildProductFilter(productFilterEnum, true));

        if (ProductFilterEnum.NULL.equals(productFilterEnum) || ProductFilterEnum.ONLY_OTHER.equals(productFilterEnum)) {
            mockMvc.perform(post("/hmr/api/products/search")
                    .with(authentication(userAuth))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productsFilter)
                    .param("page", "0")
                    .param("size", "50"))
                .andExpect(status().isNoContent());

        } else {
            mockMvc.perform(post("/hmr/api/products/search")
                    .with(authentication(userAuth))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productsFilter)
                    .param("page", "0")
                    .param("size", "50"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(expectedContentSizeResults.get(productFilterEnum)));

        }
    }

    @ParameterizedTest
    @EnumSource(ProductFilterEnum.class)
    @Order(2)
    @Transactional
    void searchProducts_withNoMatchingFilter(ProductFilterEnum productFilterEnum) throws Exception {

        String productsFilter = IntegrationTestUtils.toJson(CommonTestUtils.buildProductFilter(productFilterEnum, false));

        mockMvc.perform(post("/hmr/api/products/search")
                .with(authentication(userAuth))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(productsFilter)
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isNoContent());
    }
}
