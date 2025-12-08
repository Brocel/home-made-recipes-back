package com.example.hmrback.api.controller.recipe;

import com.example.hmrback.api.controller.RecipeBaseIntegrationTest;
import com.example.hmrback.utils.test.CommonTestUtils;
import com.example.hmrback.utils.test.IntegrationTestUtils;
import com.example.hmrback.utils.test.RecipeFilterEnum;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class RecipeControllerReadTest extends RecipeBaseIntegrationTest {

    private final Map<RecipeFilterEnum, Integer> expectedContentSizeResults = Map.of(
        RecipeFilterEnum.TITLE, 1,
        RecipeFilterEnum.DESCRIPTION, 1,
        RecipeFilterEnum.MAXIMUM_PREPARATION_TIME, 1,
        RecipeFilterEnum.RECIPE_TYPE, 1,
        RecipeFilterEnum.AUTHOR_USERNAME, 1,
        RecipeFilterEnum.INGREDIENT_NAME, 1,
        RecipeFilterEnum.INGREDIENT_TYPE, 1
    );

    @ParameterizedTest
    @EnumSource(RecipeFilterEnum.class)
    @Order(1)
    @WithMockUser(username = "username1")
    @Transactional
    void searchRecipes(RecipeFilterEnum filterEnum) throws Exception {

        when(recipeRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(savedRecipe)));

        String recipeFilters = IntegrationTestUtils.toJson(CommonTestUtils.buildRecipeFilter(filterEnum, true));
        mockMvc.perform(post("/hmr/api/recipes/search")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeFilters)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "title,asc"))
            .andExpect(status().isOk())
            .andExpect(header().exists("X-Total-Count"))
            .andExpect(jsonPath("$.content").exists())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(expectedContentSizeResults.get(filterEnum)));
    }

    @ParameterizedTest
    @EnumSource(RecipeFilterEnum.class)
    @Order(2)
    @WithMockUser(username = "username1")
    @Transactional
    void searchRecipes_withNoMatchingFilter(RecipeFilterEnum filterEnum) throws Exception {

        when(recipeRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        String recipeFilters = IntegrationTestUtils.toJson(CommonTestUtils.buildRecipeFilter(filterEnum, false));
        mockMvc.perform(post("/hmr/api/recipes/search")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeFilters)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "title,asc"))
            .andExpect(status().isNoContent());
    }
}
