package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.Ingredient;
import com.example.hmrback.model.request.CreateIngredientRequest;
import com.example.hmrback.model.request.UpdateIngredientRequest;
import com.example.hmrback.model.response.IngredientResponse;
import com.example.hmrback.utils.test.DtoTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IngredientDTOMapperTest")
class IngredientDTOMapperTest {

    @Test
    @DisplayName("Should convert CreateIngredientRequest to Ingredient with id=null")
    void shouldConvertCreateIngredientRequestToIngredientWithNullId() {
        // Given
        CreateIngredientRequest request = DtoTestUtils.buildCreateIngredientRequest(1L);

        // When
        Ingredient ingredient = IngredientDTOMapper.toDomainModel(request);

        // Then
        assertNotNull(ingredient);
        assertNull(ingredient.id());
        assertEquals(request.quantity(), ingredient.quantity());
        assertEquals(request.unit(), ingredient.unit());
        assertEquals(request.product(), ingredient.product());
    }

    @Test
    @DisplayName("Should convert UpdateIngredientRequest to Ingredient with id preserved")
    void shouldPreserveIdWhenConvertingUpdateIngredientRequest() {
        // Given
        UpdateIngredientRequest request = DtoTestUtils.buildUpdateIngredientRequest(3L);

        // When
        Ingredient ingredient = IngredientDTOMapper.toDomainModel(request);

        // Then
        assertNotNull(ingredient);
        assertEquals(request.id(), ingredient.id());
        assertEquals(request.quantity(), ingredient.quantity());
        assertEquals(request.unit(), ingredient.unit());
        assertEquals(request.product(), ingredient.product());
    }

    @Test
    @DisplayName("Should convert Ingredient to IngredientResponse with all fields preserved")
    void shouldConvertIngredientToIngredientResponse() {
        // Given
        Ingredient ingredient = ModelTestUtils.buildIngredient(1L, false);

        // When
        IngredientResponse response = IngredientDTOMapper.toDtoResponse(ingredient);

        // Then
        assertNotNull(response);
        assertEquals(ingredient.id(), response.id());
        assertEquals(ingredient.quantity(), response.quantity());
        assertEquals(ingredient.unit(), response.unit());
        assertEquals(ingredient.product(), response.product());
    }

    @Test
    @DisplayName("Should handle null CreateIngredientRequest gracefully")
    void shouldHandleNullCreateIngredientRequest() {
        // When
        Ingredient ingredient = IngredientDTOMapper.toDomainModel((CreateIngredientRequest) null);

        // Then
        assertNull(ingredient);
    }

    @Test
    @DisplayName("Should handle null UpdateIngredientRequest gracefully")
    void shouldHandleNullUpdateIngredientRequest() {
        // When
        Ingredient ingredient = IngredientDTOMapper.toDomainModel((UpdateIngredientRequest) null);

        // Then
        assertNull(ingredient);
    }

    @Test
    @DisplayName("Should handle null Ingredient gracefully in toResponse")
    void shouldHandleNullIngredientInToResponse() {
        // When
        IngredientResponse response = IngredientDTOMapper.toDtoResponse(null);

        // Then
        assertNull(response);
    }

    @Test
    @DisplayName("Should convert list of CreateIngredientRequest to Ingredients with id=null")
    void shouldConvertCreateIngredientRequestListToIngredients() {
        // Given
        List<CreateIngredientRequest> requests = DtoTestUtils.buildIngredientListForCreation(3);

        // When
        List<Ingredient> ingredients = IngredientDTOMapper.toDomainModels(requests);

        // Then
        assertNotNull(ingredients);
        assertEquals(3, ingredients.size());
        ingredients.forEach(ing -> assertNull(ing.id()));
    }

    @Test
    @DisplayName("Should convert list of UpdateIngredientRequest to Ingredients with id preserved")
    void shouldConvertUpdateIngredientRequestListToIngredients() {
        // Given
        List<UpdateIngredientRequest> requests = DtoTestUtils.buildIngredientListForUpdate(2);

        // When
        List<Ingredient> ingredients = IngredientDTOMapper.toDomainModelsFromUpdate(requests);

        // Then
        assertNotNull(ingredients);
        assertEquals(2, ingredients.size());
        assertEquals(1L, ingredients.get(0).id());
        assertEquals(2L, ingredients.get(1).id());
    }

    @Test
    @DisplayName("Should convert list of Ingredients to IngredientResponses")
    void shouldConvertIngredientListToResponses() {
        // Given
        List<Ingredient> ingredients = ModelTestUtils.buildIngredientList(3, false);

        // When
        List<IngredientResponse> responses = IngredientDTOMapper.toDtoResponses(ingredients);

        // Then
        assertNotNull(responses);
        assertEquals(3, responses.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(ingredients.get(i).id(), responses.get(i).id());
            assertEquals(ingredients.get(i).quantity(), responses.get(i).quantity());
        }
    }

    @Test
    @DisplayName("Should handle null list in toDomainModels - return empty list")
    void shouldHandleNullListInToDomainModels() {
        // When
        List<Ingredient> ingredients = IngredientDTOMapper.toDomainModels(null);

        // Then
        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty list in toDomainModels - return empty list")
    void shouldHandleEmptyListInToDomainModels() {
        // When
        List<Ingredient> ingredients = IngredientDTOMapper.toDomainModels(List.of());

        // Then
        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
    }

    @Test
    @DisplayName("Should handle null list in toDomainModelsFromUpdate - return empty list")
    void shouldHandleNullListInToDomainModelsFromUpdate() {
        // When
        List<Ingredient> ingredients = IngredientDTOMapper.toDomainModelsFromUpdate(null);

        // Then
        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
    }

    @Test
    @DisplayName("Should handle null list in toDtoResponses - return empty list")
    void shouldHandleNullListInToDtoResponses() {
        // When
        List<IngredientResponse> responses = IngredientDTOMapper.toDtoResponses(null);

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should handle different Unit types correctly through test utilities")
    void shouldHandleDifferentUnitTypesCorrectly() {
        // Given
        List<CreateIngredientRequest> requests = DtoTestUtils.buildIngredientListForCreation(3);

        // When
        List<Ingredient> ingredients = IngredientDTOMapper.toDomainModels(requests);

        // Then
        assertNotNull(ingredients);
        assertEquals(3, ingredients.size());
        // Verify different units are used
        assertNotEquals(ingredients.get(0).unit(), ingredients.get(1).unit());
        assertNotEquals(ingredients.get(1).unit(), ingredients.get(2).unit());
    }
}

