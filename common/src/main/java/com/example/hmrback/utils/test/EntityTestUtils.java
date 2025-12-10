package com.example.hmrback.utils.test;

import com.example.hmrback.persistence.entity.*;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.RecipeType;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.enums.Unit;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.LongStream;

import static com.example.hmrback.utils.test.TestConstants.*;

public class EntityTestUtils {

    private EntityTestUtils() {
    }

    /**
     * Builds a UserEntity instance for testing purposes.
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>First Name: "fName" + ordinal</li>
     *     <li>Last Name: "lName" + ordinal</li>
     *     <li>Email: "username" + ordinal + "@test.com"</li>
     *     <li>Birth Date: Current date minus (10 * ordinal) years</li>
     *     <li>Inscription Date: Current date minus ordinal months</li>
     * </ul>
     *
     * @param ordinal the position number to differentiate users
     * @return UserEntity instance
     */
    public static UserEntity buildUserEntity(Long ordinal, boolean isCreation) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(isCreation ? null : CommonTestUtils.uuidFromLong(ordinal));
        userEntity.setFirstName(FIRST_NAME.formatted(ordinal));
        userEntity.setLastName(LAST_NAME.formatted(ordinal));
        userEntity.setUsername(USERNAME.formatted(ordinal));
        userEntity.setEmail(EMAIL.formatted(ordinal));
        userEntity.setBirthDate(LocalDate.now().minusYears(10 * ordinal));
        userEntity.setInscriptionDate(LocalDate.now().minusMonths(ordinal));
        userEntity.setRoles(new HashSet<>(Collections.singleton(buildRoleEntity())));
        return userEntity;
    }

    /**
     * Builds a list of UserEntity instances for testing purposes.
     *
     * @param count the number of UserEntity instances to create
     * @return List of UserEntity instances
     */
    public static List<UserEntity> buildUserEntityList(int count) {
        return LongStream.rangeClosed(1, count)
            .mapToObj(num -> buildUserEntity(num, false))
            .toList();
    }

    /**
     * Builds a ProductEntity instance for testing purposes.
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>Name: "Product" + ordinal</li>
     *     <li>Ingredient Type: IngredientType corresponding to (ordinal - 1) index</li>
     * </ul>
     *
     * @param ordinal the position number to differentiate products
     * @return ProductEntity instance
     */
    public static ProductEntity buildProductEntity(Long ordinal, boolean isCreation) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(isCreation ? null : ordinal);
        productEntity.setName(PRODUCT_NAME.formatted(ordinal));
        productEntity.setIngredientType(IngredientType.getByIndex(ordinal.intValue() - 1));
        return productEntity;
    }

    /**
     * Builds a list of ProductEntity instances for testing purposes.
     * If the requested count exceeds the number of available IngredientType values,
     * it limits the count to the number of IngredientType values.
     *
     * @param count the number of ProductEntity instances to create
     * @return List of ProductEntity instances
     */
    public static List<ProductEntity> buildProductEntityList(int count, boolean isCreation) {
        if (count > IngredientType.values().length) {
            count = IngredientType.values().length;
        }
        return LongStream.rangeClosed(1, count)
            .mapToObj(num -> EntityTestUtils.buildProductEntity(num, isCreation))
            .toList();
    }

    /**
     * Builds a StepEntity instance for testing purposes.
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>Description: "Step description " + ordinal</li>
     * </ul>
     *
     * @param ordinal the position number to differentiate steps
     * @return StepEntity instance
     */
    public static StepEntity buildStepEntity(Long ordinal, boolean isCreation) {
        StepEntity stepEntity = new StepEntity();
        stepEntity.setId(isCreation ? null : ordinal);
        stepEntity.setDescription(STEP_DESCRIPTION.formatted(ordinal));
        stepEntity.setOrder(ordinal.intValue());
        return stepEntity;
    }

    /**
     * Builds a list of StepEntity instances for testing purposes.
     *
     * @param count the number of StepEntity instances to create
     * @return List of StepEntity instances
     */
    public static List<StepEntity> buildStepEntityList(int count, boolean isCreation) {
        return LongStream.rangeClosed(1, count)
            .mapToObj(num -> EntityTestUtils.buildStepEntity(num, isCreation))
            .toList();
    }

    /**
     * Builds an IngredientEntity instance for testing purposes.
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>Quantity: ordinal * 10.0</li>
     * </ul>
     *
     * @param ordinal the position number to differentiate ingredients
     * @return IngredientEntity instance
     */
    public static IngredientEntity buildIngredientEntity(Long ordinal, boolean isCreation) {
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(isCreation ? null : ordinal);
        ingredientEntity.setQuantity(ordinal.doubleValue() * 10);
        ingredientEntity.setUnit(Unit.getByIndex(ordinal.intValue() - 1));
        ingredientEntity.setProduct(buildProductEntity(ordinal, isCreation));
        return ingredientEntity;
    }

    /**
     * Builds a list of IngredientEntity instances for testing purposes.
     *
     * @param count the number of IngredientEntity instances to create
     * @return List of IngredientEntity instances
     */
    public static List<IngredientEntity> buildIngredientEntityList(int count, boolean isCreation) {
        if (count > Unit.values().length) {
            count = Unit.values().length;
        }
        return LongStream.rangeClosed(1, count)
            .mapToObj(num -> EntityTestUtils.buildIngredientEntity(num, isCreation))
            .toList();
    }

    /**
     * Builds a RecipeEntity instance for testing purposes.
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>Title: "Recipe " + ordinal</li>
     *     <li>Description: "Recipe description " + ordinal</li>
     *     <li>Recipe Type: RecipeType corresponding to (ordinal - 1) index</li>
     *     <li>Preparation Time: ordinal * 40</li>
     *     <li>Publication Date: Current date minus (10 * ordinal) days</li>
     * </ul>
     *
     * @param ordinal the position number to differentiate recipes
     * @return RecipeEntity instance
     */
    public static RecipeEntity buildRecipeEntity(Long ordinal, boolean isCreation) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(isCreation ? null : ordinal);
        recipeEntity.setTitle(RECIPE_TITLE.formatted(ordinal));
        recipeEntity.setDescription(RECIPE_DESCRIPTION.formatted(ordinal));
        recipeEntity.setRecipeType(RecipeType.getByIndex(ordinal.intValue() - 1));
        recipeEntity.setPreparationTime(ordinal.intValue() * 40);
        recipeEntity.setPublicationDate(LocalDate.now().minusDays(10 * ordinal));
        return recipeEntity;
    }

    /**
     * Builds a RecipeEntity instance for integration testing purposes.
     *
     * @return RecipeEntity instance
     */
    public static RecipeEntity buildRecipeEntityIT() {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setTitle(RECIPE_TITLE.formatted("IT"));
        recipeEntity.setDescription(RECIPE_DESCRIPTION.formatted("IT"));
        recipeEntity.setRecipeType(RecipeType.APPETIZER);
        recipeEntity.setPreparationTime(20);
        recipeEntity.setPublicationDate(LocalDate.now());
        return recipeEntity;
    }

    /**
     * Builds a list of RecipeEntity instances for testing purposes.
     * If the requested count exceeds the number of available RecipeType values,
     * it limits the count to the number of RecipeType values.
     *
     * @param count the number of RecipeEntity instances to create
     * @return List of RecipeEntity instances
     */
    public static List<RecipeEntity> buildRecipeEntityList(int count, boolean isCreation) {
        if (count > RecipeType.values().length) {
            count = RecipeType.values().length;
        }
        return LongStream.rangeClosed(1, count)
            .mapToObj(num -> EntityTestUtils.buildRecipeEntity(num, isCreation))
            .toList();
    }

    /**
     * Builds a RoleEntity instance for testing purposes (ROLE_USER).
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>Name: from enum</li>
     * </ul>
     *
     * @return RoleEntity instance
     */
    public static RoleEntity buildRoleEntity() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1L);
        entity.setName(RoleEnum.ROLE_USER);
        return entity;
    }

    /**
     * Builds a RoleEntity instance for testing purposes.
     * <ul>
     *     <li>Name: ROLE_ADMIN if isAdmin is true, otherwise ROLE_USER</li>
     * </ul>
     *
     * @param isAdmin flag to determine role type
     * @return RoleEntity instance
     */
    public static RoleEntity buildRoleEntity(boolean isAdmin) {
        RoleEntity entity = new RoleEntity();
        entity.setName(isAdmin ? RoleEnum.ROLE_ADMIN : RoleEnum.ROLE_USER);
        return entity;
    }
}
