package com.example.hmrback.utils.test;

import com.example.hmrback.model.request.*;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.RecipeType;
import com.example.hmrback.persistence.enums.Unit;
import com.example.hmrback.utils.DateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;

import static com.example.hmrback.utils.test.TestConstants.*;

/**
 * Utility class for building request and response DTOs for testing.
 * <p>
 * This class mirrors {@link ModelTestUtils} but operates at the DTO layer,
 * providing builders for API request objects (CreateXxxRequest, UpdateXxxRequest).
 */
public class DtoTestUtils {

    private DtoTestUtils() {
    }

    // ============ RECIPES ============

    /**
     * Build a CreateRecipeRequest object for testing.
     *
     * <ul>
     *     <li>Title: "Recipe title " + ordinal</li>
     *     <li>Description: "Recipe description " + ordinal</li>
     *     <li>Preparation Time: ordinal * 40 minutes</li>
     *     <li>Recipe Type: APPETIZER</li>
     *     <li>Publication Date: Current date minus (10 * ordinal) days</li>
     *     <li>Ingredients: 4 CreateIngredientRequest objects</li>
     *     <li>Steps: 5 CreateStepRequest objects</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate recipes
     * @return the CreateRecipeRequest object
     */
    public static CreateRecipeRequest buildCreateRecipeRequest(Long ordinal) {
        return buildCreateRecipeRequest(ordinal, RecipeType.APPETIZER, 4, 5);
    }

    /**
     * Build a CreateRecipeRequest object for testing with custom configuration.
     *
     * @param ordinal         the ordinal to differentiate recipes
     * @param recipeType      the recipe type
     * @param ingredientCount the number of ingredients to include
     * @param stepCount       the number of steps to include
     * @return the CreateRecipeRequest object
     */
    public static CreateRecipeRequest buildCreateRecipeRequest(
            Long ordinal,
            RecipeType recipeType,
            int ingredientCount,
            int stepCount) {
        return new CreateRecipeRequest(
                RECIPE_TITLE.formatted(ordinal),
                RECIPE_DESCRIPTION.formatted(ordinal),
                ordinal.intValue() * 40,
                recipeType,
                DateUtils.formatLocalDate(LocalDate.now().minusDays(10 * ordinal)),
                buildIngredientListForCreation(ingredientCount),
                buildStepListForCreation(stepCount));
    }

    /**
     * Build an UpdateRecipeRequest object for testing.
     *
     * <ul>
     *     <li>Title: "Recipe title " + ordinal</li>
     *     <li>Description: "Recipe description " + ordinal</li>
     *     <li>Preparation Time: ordinal * 40 minutes</li>
     *     <li>Recipe Type: APPETIZER</li>
     *     <li>Publication Date: Current date minus (10 * ordinal) days</li>
     *     <li>Ingredients: 4 UpdateIngredientRequest objects (with IDs)</li>
     *     <li>Steps: 5 UpdateStepRequest objects (with IDs)</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate recipes
     * @return the UpdateRecipeRequest object
     */
    public static UpdateRecipeRequest buildUpdateRecipeRequest(Long ordinal) {
        return buildUpdateRecipeRequest(ordinal, RecipeType.APPETIZER, 4, 5);
    }

    /**
     * Build an UpdateRecipeRequest object for testing with custom configuration.
     *
     * @param ordinal         the ordinal to differentiate recipes
     * @param recipeType      the recipe type
     * @param ingredientCount the number of ingredients to include
     * @param stepCount       the number of steps to include
     * @return the UpdateRecipeRequest object
     */
    public static UpdateRecipeRequest buildUpdateRecipeRequest(
            Long ordinal,
            RecipeType recipeType,
            int ingredientCount,
            int stepCount) {
        return new UpdateRecipeRequest(
                RECIPE_TITLE.formatted(ordinal),
                RECIPE_DESCRIPTION.formatted(ordinal),
                ordinal.intValue() * 40,
                recipeType,
                DateUtils.formatLocalDate(LocalDate.now().minusDays(10 * ordinal)),
                buildIngredientListForUpdate(ingredientCount),
                buildStepListForUpdate(stepCount));
    }

    // ============ INGREDIENTS ============

    /**
     * Build a CreateIngredientRequest object for testing.
     *
     * <ul>
     *     <li>Quantity: ordinal * 10</li>
     *     <li>Unit: Unit corresponding to (ordinal - 1) index</li>
     *     <li>Product: Product with id = ordinal</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate ingredients
     * @return the CreateIngredientRequest object
     */
    public static CreateIngredientRequest buildCreateIngredientRequest(Long ordinal) {
        return new CreateIngredientRequest(
                ordinal.doubleValue() * 10,
                Unit.getByIndex(ordinal.intValue() - 1),
                ModelTestUtils.buildProduct(ordinal));
    }

    /**
     * Build a list of CreateIngredientRequest objects for testing.
     * <p>
     * Caps the count at the number of available Unit values to ensure valid test data.
     *
     * @param count the requested number of ingredients
     * @return the list of CreateIngredientRequest objects
     */
    public static List<CreateIngredientRequest> buildIngredientListForCreation(int count) {
        count = capCount(count, Unit.values().length);
        return LongStream.rangeClosed(1L, count)
                .mapToObj(DtoTestUtils::buildCreateIngredientRequest)
                .toList();
    }

    /**
     * Build an UpdateIngredientRequest object for testing.
     *
     * <ul>
     *     <li>ID: ordinal</li>
     *     <li>Quantity: ordinal * 10</li>
     *     <li>Unit: Unit corresponding to (ordinal - 1) index</li>
     *     <li>Product: Product with id = ordinal</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate ingredients
     * @return the UpdateIngredientRequest object
     */
    public static UpdateIngredientRequest buildUpdateIngredientRequest(Long ordinal) {
        return new UpdateIngredientRequest(
                ordinal,
                ordinal.doubleValue() * 10,
                Unit.getByIndex(ordinal.intValue() - 1),
                ModelTestUtils.buildProduct(ordinal));
    }

    /**
     * Build a list of UpdateIngredientRequest objects for testing.
     * <p>
     * Caps the count at the number of available Unit values to ensure valid test data.
     *
     * @param count the requested number of ingredients
     * @return the list of UpdateIngredientRequest objects
     */
    public static List<UpdateIngredientRequest> buildIngredientListForUpdate(int count) {
        count = capCount(count, Unit.values().length);
        return LongStream.rangeClosed(1L, count)
                .mapToObj(DtoTestUtils::buildUpdateIngredientRequest)
                .toList();
    }

    // ============ STEPS ============

    /**
     * Build a CreateStepRequest object for testing.
     *
     * <ul>
     *     <li>Description: "Step description " + ordinal</li>
     *     <li>Order: ordinal as integer</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate steps
     * @return the CreateStepRequest object
     */
    public static CreateStepRequest buildCreateStepRequest(Long ordinal) {
        return new CreateStepRequest(
                STEP_DESCRIPTION.formatted(ordinal),
                ordinal.intValue());
    }

    /**
     * Build a list of CreateStepRequest objects for testing.
     *
     * @param count the number of steps to create
     * @return the list of CreateStepRequest objects
     */
    public static List<CreateStepRequest> buildStepListForCreation(int count) {
        return LongStream.rangeClosed(1L, count)
                .mapToObj(DtoTestUtils::buildCreateStepRequest)
                .toList();
    }

    /**
     * Build an UpdateStepRequest object for testing.
     *
     * <ul>
     *     <li>ID: ordinal</li>
     *     <li>Description: "Step description " + ordinal</li>
     *     <li>Order: ordinal as integer</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate steps
     * @return the UpdateStepRequest object
     */
    public static UpdateStepRequest buildUpdateStepRequest(Long ordinal) {
        return new UpdateStepRequest(
                ordinal,
                STEP_DESCRIPTION.formatted(ordinal),
                ordinal.intValue());
    }

    /**
     * Build a list of UpdateStepRequest objects for testing.
     *
     * @param count the number of steps to create
     * @return the list of UpdateStepRequest objects
     */
    public static List<UpdateStepRequest> buildStepListForUpdate(int count) {
        return LongStream.rangeClosed(1L, count)
                .mapToObj(DtoTestUtils::buildUpdateStepRequest)
                .toList();
    }

    // ============ PRODUCTS ============

    /**
     * Build a CreateProductRequest object for testing.
     *
     * <ul>
     *     <li>Name: "Carrot" if existingProduct is true, otherwise "FAKE"</li>
     *     <li>Ingredient Type: VEGETABLE if existingProduct is true, otherwise OTHER</li>
     * </ul>
     *
     * @param existingProduct if true, builds a request for an existing product; if false, builds for a new product
     * @return the CreateProductRequest object
     */
    public static CreateProductRequest buildCreateProductRequest(boolean existingProduct) {
        return new CreateProductRequest(
                existingProduct ? "Carrot" : FAKE,
                existingProduct ? IngredientType.VEGETABLE : IngredientType.OTHER);
    }

    /**
     * Build an UpdateProductRequest object for testing.
     * <p>
     * Uses the same logic as {@link #buildCreateProductRequest(boolean)} since
     * the request structure is identical.
     *
     * @param ordinal id of the product
     * @return the UpdateProductRequest object
     */
    public static UpdateProductRequest buildUpdateProductRequest(Long ordinal) {
        return new UpdateProductRequest(
                ordinal,
                "Carrot" + ordinal,
                IngredientType.VEGETABLE);
    }

    // ============ AUTHENTICATION ============

    /**
     * Build a RegisterRequest object for testing.
     *
     * <ul>
     *     <li>First Name: "Test" if valid is true, otherwise null (for validation testing)</li>
     *     <li>Last Name: "TEST"</li>
     *     <li>Username: "Test666"</li>
     *     <li>Email: "valid.email@domain.com"</li>
     *     <li>Password: "123456"</li>
     *     <li>Birth Date: "01/01/1966"</li>
     * </ul>
     *
     * @param valid if true, all fields are valid; if false, firstName is set to null for validation testing
     * @return the RegisterRequest object
     */
    public static RegisterRequest buildRegisterRequest(boolean valid) {
        return new RegisterRequest(
                valid ? FIRST_NAME.formatted(1L) : null,
                LAST_NAME.formatted(1L),
                USERNAME.formatted(1L),
                EMAIL.formatted(1L),
                "123456",
                "01/01/1966");
    }

    /**
     * Build a LoginRequest object for testing.
     *
     * <ul>
     *     <li>Email: "valid.email@domain.com"</li>
     *     <li>Password: "123456" if valid is true, otherwise "bouh!" (for authentication testing)</li>
     * </ul>
     *
     * @param valid if true, provides valid credentials; if false, provides an invalid password
     * @return the LoginRequest object
     */
    public static LoginRequest buildLoginRequest(boolean valid) {
        return new LoginRequest(
                EMAIL.formatted(1L),
                valid ? "123456" : "invalidPassword");
    }

    // ============ HELPERS ============

    /**
     * Cap a count at a maximum value.
     * <p>
     * Useful for ensuring test data doesn't exceed available enum values.
     *
     * @param requested the requested count
     * @param maximum   the maximum allowed count
     * @return the capped count
     */
    private static int capCount(int requested, int maximum) {
        return Math.min(requested, maximum);
    }
}
