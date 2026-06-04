package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.Recipe;
import com.example.hmrback.model.request.CreateRecipeRequest;
import com.example.hmrback.model.request.UpdateRecipeRequest;
import com.example.hmrback.model.response.RecipeResponse;

import java.util.ArrayList;

/**
 * Orchestrates Recipe domain model transformations with DTOs.
 *
 * This mapper handles Recipe conversions and delegates to nested mappers
 * (UserDTOMapper, IngredientDTOMapper, StepDTOMapper) for complete
 * transformations. It manages:
 *
 * - Create flow: CreateRecipeRequest → Recipe (id=null, author=null)
 * - Update flow: UpdateRecipeRequest + id → Recipe (id preserved)
 * - Read flow: Recipe → RecipeResponse (with nested conversions)
 *
 * All methods are null-safe and handle empty collections gracefully.
 * No business logic is performed; transformations are purely structural.
 *
 * Usage:
 * <pre>
 *   // Create flow
 *   Recipe newRecipe = RecipeDTOMapper.toRecipe(request);
 *
 *   // Response flow
 *   RecipeResponse response = RecipeDTOMapper.toResponse(persistedRecipe);
 * </pre>
 *
 * @since 1.0
 */
public final class RecipeDTOMapper {

    private RecipeDTOMapper() {
        // Utility class, no instantiation
    }

    /**
     * Converts a CreateRecipeRequest to a Recipe domain model.
     *
     * The resulting Recipe will have:
     * - id=null (for new recipes)
     * - author=null (will be set by service based on username)
     * - ingredientList and stepList converted via nested mappers
     *
     * @param request the CreateRecipeRequest to convert (may be null)
     * @return Recipe domain model with id=null, author=null, or null if input is null
     */
    public static Recipe toRecipe(CreateRecipeRequest request) {
        if (request == null) {
            return null;
        }

        return new Recipe(
                null,                                                           // id is null for new recipes
                request.title(),
                request.description(),
                request.preparationTime(),
                request.recipeType(),
                request.publicationDate(),
                null,                                                           // author is null; service will set it
                IngredientDTOMapper.toDomainModels(request.ingredientList()),
                StepDTOMapper.toDomainModels(request.stepList())
        );
    }

    /**
     * Converts an UpdateRecipeRequest to a Recipe domain model.
     *
     * The resulting Recipe will have:
     * - id=null during the conversion (caller must set the id if needed)
     * - author=null (will be preserved by service)
     * - ingredientList and stepList converted via nested mappers
     *
     * Note: The service layer is responsible for id validation and author preservation.
     *
     * @param request the UpdateRecipeRequest to convert (may be null)
     * @return Recipe domain model with id=null, author=null, or null if input is null
     */
    public static Recipe toRecipe(UpdateRecipeRequest request) {
        if (request == null) {
            return null;
        }

        return new Recipe(
                null,                                                           // id is null; service provides it
                request.title(),
                request.description(),
                request.preparationTime(),
                request.recipeType(),
                request.publicationDate(),
                null,                                                           // author is null; service preserves existing
                IngredientDTOMapper.toDomainModelsFromUpdate(request.ingredientList()),
                StepDTOMapper.toDomainModelsFromUpdate(request.stepList())
        );
    }

    /**
     * Converts a Recipe domain model to a RecipeResponse DTO.
     *
     * All fields are converted. The author (User) is converted to UserResponse via UserDTOMapper.
     * Ingredient and Step lists are passed through as-is (RecipeResponse accepts the domain models).
     *
     * @param recipe the Recipe domain model to convert (may be null)
     * @return RecipeResponse DTO, or null if input is null
     */
    public static RecipeResponse toResponse(Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        return new RecipeResponse(
                recipe.id(),
                recipe.title(),
                recipe.description(),
                recipe.preparationTime(),
                recipe.recipeType(),
                recipe.publicationDate(),
                UserDTOMapper.toDtoResponse(recipe.author()),
                recipe.ingredientList() != null ? recipe.ingredientList() : new ArrayList<>(),
                recipe.stepList() != null ? recipe.stepList() : new ArrayList<>()
        );
    }
}
