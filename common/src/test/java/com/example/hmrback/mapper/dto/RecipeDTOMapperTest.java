package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.Recipe;
import com.example.hmrback.model.User;
import com.example.hmrback.model.request.CreateRecipeRequest;
import com.example.hmrback.model.request.UpdateRecipeRequest;
import com.example.hmrback.model.response.RecipeResponse;
import com.example.hmrback.persistence.enums.RecipeType;
import com.example.hmrback.utils.test.DtoTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.example.hmrback.utils.test.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RecipeDTOMapperTest")
class RecipeDTOMapperTest {

    @Test
    @DisplayName("Should convert CreateRecipeRequest to Recipe with id=null and author=null")
    void shouldConvertCreateRecipeRequestToRecipeWithNullIdAndAuthor() {
        // Given
        CreateRecipeRequest request = DtoTestUtils.buildCreateRecipeRequest(1L, RecipeType.APPETIZER, 2, 2);

        // When
        Recipe recipe = RecipeDTOMapper.toRecipe(request);

        // Then
        assertNotNull(recipe);
        assertNull(recipe.id());
        assertEquals(request.title(), recipe.title());
        assertEquals(request.description(), recipe.description());
        assertEquals(request.preparationTime(), recipe.preparationTime());
        assertEquals(request.recipeType(), recipe.recipeType());
        assertEquals(request.publicationDate(), recipe.publicationDate());
        assertNull(recipe.author());
        assertNotNull(recipe.ingredientList());
        assertFalse(recipe.ingredientList().isEmpty());
        assertNotNull(recipe.stepList());
        assertFalse(recipe.stepList().isEmpty());
    }

    @Test
    @DisplayName("Should convert UpdateRecipeRequest to Recipe with id=null and author=null")
    void shouldConvertUpdateRecipeRequestToRecipeWithNullIdAndAuthor() {
        // Given
        UpdateRecipeRequest request = DtoTestUtils.buildUpdateRecipeRequest(2L, RecipeType.MAIN_COURSE, 3, 3);

        // When
        Recipe recipe = RecipeDTOMapper.toRecipe(request);

        // Then
        assertNotNull(recipe);
        assertNull(recipe.id());
        assertEquals(request.title(), recipe.title());
        assertEquals(request.description(), recipe.description());
        assertEquals(request.preparationTime(), recipe.preparationTime());
        assertEquals(request.recipeType(), recipe.recipeType());
        assertEquals(request.publicationDate(), recipe.publicationDate());
        assertNull(recipe.author());
        assertNotNull(recipe.ingredientList());
        assertNotNull(recipe.stepList());
    }

    @Test
    @DisplayName("Should convert Recipe to RecipeResponse with all fields preserved")
    void shouldConvertRecipeToRecipeResponse() {
        // Given
        Recipe recipe = ModelTestUtils.buildRecipe(1L, false);

        // When
        RecipeResponse response = RecipeDTOMapper.toResponse(recipe);

        // Then
        assertNotNull(response);
        assertEquals(recipe.id(), response.id());
        assertEquals(recipe.title(), response.title());
        assertEquals(recipe.description(), response.description());
        assertEquals(recipe.preparationTime(), response.preparationTime());
        assertEquals(recipe.recipeType(), response.recipeType());
        assertEquals(recipe.publicationDate(), response.publicationDate());
        assertNotNull(response.author());
        assertNotNull(response.ingredientList());
        assertNotNull(response.stepList());
    }

    @Test
    @DisplayName("Should handle null Recipe gracefully in toResponse")
    void shouldHandleNullRecipeInToResponse() {
        // When
        RecipeResponse response = RecipeDTOMapper.toResponse(null);

        // Then
        assertNull(response);
    }

    @Test
    @DisplayName("Should handle null CreateRecipeRequest gracefully")
    void shouldHandleNullCreateRecipeRequest() {
        // When
        Recipe recipe = RecipeDTOMapper.toRecipe((CreateRecipeRequest) null);

        // Then
        assertNull(recipe);
    }

    @Test
    @DisplayName("Should handle null UpdateRecipeRequest gracefully")
    void shouldHandleNullUpdateRecipeRequest() {
        // When
        Recipe recipe = RecipeDTOMapper.toRecipe((UpdateRecipeRequest) null);

        // Then
        assertNull(recipe);
    }

    @Test
    @DisplayName("Should handle null author in Recipe response conversion")
    void shouldHandleNullAuthorInResponseConversion() {
        // Given
        Recipe recipe = new Recipe(
                1L, "Recipe", "Description", 30, RecipeType.MAIN_COURSE, "2024-01-01",
                null,  // null author
                List.of(),
                List.of()
        );

        // When
        RecipeResponse response = RecipeDTOMapper.toResponse(recipe);

        // Then
        assertNotNull(response);
        assertNull(response.author());
    }

    @Test
    @DisplayName("Should handle null ingredient list in response conversion")
    void shouldHandleNullIngredientListInResponseConversion() {
        // Given
        User author = ModelTestUtils.buildUser(1L, false);
        Recipe recipe = new Recipe(
                1L, "Recipe", "Description", 30, RecipeType.MAIN_COURSE, "2024-01-01",
                author,
                null,  // null ingredient list
                List.of()
        );

        // When
        RecipeResponse response = RecipeDTOMapper.toResponse(recipe);

        // Then
        assertNotNull(response);
        assertNotNull(response.ingredientList());
        assertTrue(response.ingredientList().isEmpty());
    }

    @Test
    @DisplayName("Should handle null step list in response conversion")
    void shouldHandleNullStepListInResponseConversion() {
        // Given
        User author = ModelTestUtils.buildUser(1L, false);
        Recipe recipe = new Recipe(
                1L, "Recipe", "Description", 30, RecipeType.MAIN_COURSE, "2024-01-01",
                author,
                List.of(),
                null  // null step list
        );

        // When
        RecipeResponse response = RecipeDTOMapper.toResponse(recipe);

        // Then
        assertNotNull(response);
        assertNotNull(response.stepList());
        assertTrue(response.stepList().isEmpty());
    }

    @Test
    @DisplayName("Should orchestrate nested MapperCalls for ingredients in create flow")
    void shouldOrchestrateNestedIngredientsMapperCallsInCreateFlow() {
        // Given
        CreateRecipeRequest request = DtoTestUtils.buildCreateRecipeRequest(
                1L, RecipeType.APPETIZER, 3, 1);

        // When
        Recipe recipe = RecipeDTOMapper.toRecipe(request);

        // Then
        assertNotNull(recipe.ingredientList());
        assertEquals(3, recipe.ingredientList().size());
        // Verify id=null for all ingredients (create flow)
        recipe.ingredientList().forEach(ing -> assertNull(ing.id()));
    }

    @Test
    @DisplayName("Should orchestrate nested MapperCalls for steps in create flow")
    void shouldOrchestrateNestedStepsMapperCallsInCreateFlow() {
        // Given
        CreateRecipeRequest request = DtoTestUtils.buildCreateRecipeRequest(
                1L, RecipeType.APPETIZER, 1, 4);

        // When
        Recipe recipe = RecipeDTOMapper.toRecipe(request);

        // Then
        assertNotNull(recipe.stepList());
        assertEquals(4, recipe.stepList().size());
        // Verify id=null for all steps (create flow)
        recipe.stepList().forEach(step -> assertNull(step.id()));
    }

    @Test
    @DisplayName("Should orchestrate nested MapperCalls for ingredients in update flow")
    void shouldOrchestrateNestedIngredientsMapperCallsInUpdateFlow() {
        // Given
        UpdateRecipeRequest request = DtoTestUtils.buildUpdateRecipeRequest(
                2L, RecipeType.MAIN_COURSE, 3, 1);

        // When
        Recipe recipe = RecipeDTOMapper.toRecipe(request);

        // Then
        assertNotNull(recipe.ingredientList());
        assertEquals(3, recipe.ingredientList().size());
        // Verify id is preserved for all ingredients (update flow)
        recipe.ingredientList().forEach(ing -> assertNotNull(ing.id()));
    }

    @Test
    @DisplayName("Should orchestrate nested MapperCalls for steps in update flow")
    void shouldOrchestrateNestedStepsMapperCallsInUpdateFlow() {
        // Given
        UpdateRecipeRequest request = DtoTestUtils.buildUpdateRecipeRequest(
                2L, RecipeType.DESSERT, 1, 3);

        // When
        Recipe recipe = RecipeDTOMapper.toRecipe(request);

        // Then
        assertNotNull(recipe.stepList());
        assertEquals(3, recipe.stepList().size());
        // Verify id is preserved for all steps (update flow)
        recipe.stepList().forEach(step -> assertNotNull(step.id()));
    }

    @Test
    @DisplayName("Should orchestrate UserMapper in response conversion")
    void shouldOrchestrateUserMapperInResponseConversion() {
        // Given
        Recipe recipe = ModelTestUtils.buildRecipe(TestConstants.NUMBER_1, false);

        // When
        RecipeResponse response = RecipeDTOMapper.toResponse(recipe);

        // Then
        assertNotNull(response.author());
        assertEquals(recipe.author().id(), response.author().id());
        assertEquals(recipe.author().firstName(), response.author().firstName());
        assertEquals(recipe.author().lastName(), response.author().lastName());
        assertEquals(recipe.author().username(), response.author().username());
        assertEquals(recipe.author().email(), response.author().email());
    }

    @Test
    @DisplayName("Should handle empty ingredient and step lists correctly")
    void shouldHandleEmptyListsCorrectly() {
        // Given
        CreateRecipeRequest request = DtoTestUtils.buildCreateRecipeRequest(
                1L, RecipeType.APPETIZER, 0, 0);

        // When
        Recipe recipe = RecipeDTOMapper.toRecipe(request);

        // Then
        assertNotNull(recipe.ingredientList());
        assertTrue(recipe.ingredientList().isEmpty());
        assertNotNull(recipe.stepList());
        assertTrue(recipe.stepList().isEmpty());
    }

    @Test
    @DisplayName("Should preserve all recipe fields from request to domain model")
    void shouldPreserveAllRecipeFieldsFromRequestToDomain() {
        // Given
        CreateRecipeRequest request = DtoTestUtils.buildCreateRecipeRequest(1L);

        // When
        Recipe recipe = RecipeDTOMapper.toRecipe(request);

        // Then
        assertAll(
                () -> assertNull(recipe.id(), "ID should be null for create flow"),
                () -> assertEquals(request.title(), recipe.title(), "Title should match"),
                () -> assertEquals(request.description(), recipe.description(), "Description should match"),
                () -> assertEquals(request.preparationTime(), recipe.preparationTime(), "Preparation time should match"),
                () -> assertEquals(request.recipeType(), recipe.recipeType(), "Recipe type should match"),
                () -> assertEquals(request.publicationDate(), recipe.publicationDate(), "Publication date should match"),
                () -> assertNull(recipe.author(), "Author should be null in create flow"),
                () -> assertNotNull(recipe.ingredientList(), "Ingredient list should not be null"),
                () -> assertNotNull(recipe.stepList(), "Step list should not be null")
        );
    }

    @Test
    @DisplayName("Should preserve all recipe fields from domain model to response")
    void shouldPreserveAllRecipeFieldsFromDomainToResponse() {
        // Given
        Recipe recipe = ModelTestUtils.buildRecipe(3L, false);

        // When
        RecipeResponse response = RecipeDTOMapper.toResponse(recipe);

        // Then
        assertAll(
                () -> assertEquals(recipe.id(), response.id(), "ID should be preserved"),
                () -> assertEquals(recipe.title(), response.title(), "Title should be preserved"),
                () -> assertEquals(recipe.description(), response.description(), "Description should be preserved"),
                () -> assertEquals(recipe.preparationTime(), response.preparationTime(), "Preparation time should be preserved"),
                () -> assertEquals(recipe.recipeType(), response.recipeType(), "Recipe type should be preserved"),
                () -> assertEquals(recipe.publicationDate(), response.publicationDate(), "Publication date should be preserved"),
                () -> assertNotNull(response.author(), "Author should be converted to UserResponse"),
                () -> assertEquals(recipe.ingredientList().size(), response.ingredientList().size(), "Ingredient counts should match"),
                () -> assertEquals(recipe.stepList().size(), response.stepList().size(), "Step counts should match")
        );
    }
}


