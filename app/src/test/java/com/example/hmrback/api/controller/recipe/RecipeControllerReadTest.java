package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.RecipeFilterEnum;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class RecipeControllerReadTest extends RecipeBaseIntegrationTest {

    private final Map<RecipeFilterEnum, Integer> expectedContentSizeResults = Map.of(
            RecipeFilterEnum.TITLE,
            2,
            RecipeFilterEnum.DESCRIPTION,
            2,
            RecipeFilterEnum.MAXIMUM_PREPARATION_TIME,
            1,
            RecipeFilterEnum.RECIPE_TYPE,
            1,
            RecipeFilterEnum.AUTHOR_USERNAME,
            1,
            RecipeFilterEnum.INGREDIENT_NAME,
            1,
            RecipeFilterEnum.INGREDIENT_TYPE,
            1
    );

    @ParameterizedTest
    @EnumSource(RecipeFilterEnum.class)
    @Order(1)
    @Transactional
    void searchRecipes(RecipeFilterEnum filterEnum) throws Exception {

        String recipeFilters = IntegrationTestUtils.toJson(CommonTestUtils.buildRecipeFilter(filterEnum,
                                                                                             true));
        mockMvc.perform(post("/hmr/api/recipes/search")
                                .header("Authorization",
                                        "Bearer " + userToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(recipeFilters)
                                .param("page",
                                       "0")
                                .param("size",
                                       "10")
                                .param("sort",
                                       "title,asc"))
               .andExpect(status().isOk())
               .andExpect(header().exists("X-Total-Count"))
               .andExpect(jsonPath("$.content").exists())
               .andExpect(jsonPath("$.content").isArray())
               .andExpect(jsonPath("$.content.length()").value(expectedContentSizeResults.get(filterEnum)));
    }

    @ParameterizedTest
    @EnumSource(RecipeFilterEnum.class)
    @Order(2)
    @Transactional
    void searchRecipes_withNoMatchingFilter(RecipeFilterEnum filterEnum) throws Exception {

        String recipeFilters = IntegrationTestUtils.toJson(CommonTestUtils.buildRecipeFilter(filterEnum,
                                                                                             false));
        mockMvc.perform(post("/hmr/api/recipes/search")
                                .header("Authorization",
                                        "Bearer " + userToken)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(recipeFilters)
                                .param("page",
                                       "0")
                                .param("size",
                                       "10")
                                .param("sort",
                                       "title,asc"))
               .andExpect(status().isNoContent());
    }
}
