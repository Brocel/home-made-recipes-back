package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.Ingredient;
import com.example.hmrback.model.request.CreateIngredientRequest;
import com.example.hmrback.model.request.UpdateIngredientRequest;
import com.example.hmrback.model.response.IngredientResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps Ingredient domain models to/from IngredientResponse and Ingredient request DTOs.
 *
 * This utility mapper provides stateless, static transformations for Ingredient entities:
 * - CreateIngredientRequest → Ingredient (with id=null for new ingredients)
 * - UpdateIngredientRequest → Ingredient (with id preserved from request)
 * - Ingredient → IngredientResponse (for API responses)
 * - Batch operations for collections
 *
 * Product references are passed through as-is; all methods are null-safe
 * and handle empty collections gracefully.
 *
 * @since 1.0
 */
public final class IngredientDTOMapper {

    private IngredientDTOMapper() {
        // Utility class, no instantiation
    }

    /**
     * Converts a CreateIngredientRequest to an Ingredient domain model.
     *
     * The resulting Ingredient will have id=null (for new ingredients).
     *
     * @param request the CreateIngredientRequest to convert (may be null)
     * @return Ingredient domain model with id=null, or null if input is null
     */
    public static Ingredient toDomainModel(CreateIngredientRequest request) {
        if (request == null) {
            return null;
        }

        return new Ingredient(
                null,                      // id is null for new ingredients
                request.quantity(),
                request.unit(),
                request.product()
        );
    }

    /**
     * Converts an UpdateIngredientRequest to an Ingredient domain model.
     *
     * The resulting Ingredient will have the id preserved from the request.
     *
     * @param request the UpdateIngredientRequest to convert (may be null)
     * @return Ingredient domain model with id preserved, or null if input is null
     */
    public static Ingredient toDomainModel(UpdateIngredientRequest request) {
        if (request == null) {
            return null;
        }

        return new Ingredient(
                request.id(),              // id is preserved from request
                request.quantity(),
                request.unit(),
                request.product()
        );
    }

    /**
     * Converts an Ingredient domain model to an IngredientResponse DTO.
     *
     * All fields are preserved: id, quantity, unit, and product.
     *
     * @param ingredient the Ingredient domain model to convert (may be null)
     * @return IngredientResponse DTO, or null if input is null
     */
    public static IngredientResponse toDtoResponse(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        return new IngredientResponse(
                ingredient.id(),
                ingredient.quantity(),
                ingredient.unit(),
                ingredient.product()
        );
    }

    /**
     * Converts a list of CreateIngredientRequest objects to Ingredient domain models.
     *
     * @param requests the list of CreateIngredientRequest objects (may be null or empty)
     * @return list of Ingredient domain models, or empty list if input is null/empty
     */
    public static List<Ingredient> toDomainModels(List<CreateIngredientRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream()
                .map(IngredientDTOMapper::toDomainModel)
                .toList();
    }

    /**
     * Converts a list of UpdateIngredientRequest objects to Ingredient domain models.
     *
     * @param requests the list of UpdateIngredientRequest objects (may be null or empty)
     * @return list of Ingredient domain models, or empty list if input is null/empty
     */
    public static List<Ingredient> toDomainModelsFromUpdate(List<UpdateIngredientRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream()
                .map(IngredientDTOMapper::toDomainModel)
                .toList();
    }

    /**
     * Converts a list of Ingredient domain models to IngredientResponse DTOs.
     *
     * @param ingredients the list of Ingredient domain models (may be null or empty)
     * @return list of IngredientResponse DTOs, or empty list if input is null/empty
     */
    public static List<IngredientResponse> toDtoResponses(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return new ArrayList<>();
        }

        return ingredients.stream()
                .map(IngredientDTOMapper::toDtoResponse)
                .toList();
    }
}

