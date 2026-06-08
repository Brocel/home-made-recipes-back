package com.example.hmrback.persistence.util;

import com.example.hmrback.persistence.entity.*;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.RecipeType;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.enums.Unit;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.LongStream;

/**
 * Self-contained test data builder for repository-specific testing scenarios.
 * Handles transactional test data construction with entity relationships,
 * constraint violation scenarios, and cascade operation testing.
 * No external dependencies - directly instantiates and configures all entities.
 */
public class RepositoryTestDataBuilder {

    private RepositoryTestDataBuilder() {
    }

    // ==================== CRUD & Basic Entity Builders ====================

    /**
     * Builds a basic RoleEntity with ROLE_USER.
     *
     * @return new RoleEntity instance
     */
    public static RoleEntity buildRole() {
        RoleEntity role = new RoleEntity();
        role.setName(RoleEnum.ROLE_USER);
        return role;
    }

    /**
     * Builds a RoleEntity with ROLE_ADMIN.
     *
     * @return new RoleEntity instance with ROLE_ADMIN
     */
    public static RoleEntity buildAdminRole() {
        RoleEntity role = new RoleEntity();
        role.setName(RoleEnum.ROLE_ADMIN);
        return role;
    }

    /**
     * Builds a RoleEntity with specified role type.
     *
     * @param roleEnum the RoleEnum value
     * @return new RoleEntity instance
     */
    public static RoleEntity buildRoleByType(RoleEnum roleEnum) {
        RoleEntity role = new RoleEntity();
        role.setName(roleEnum);
        return role;
    }

    /**
     * Builds a basic UserEntity suitable for creation.
     *
     * @return new UserEntity instance without ID and without roles
     */
    public static UserEntity buildUser() {
        UserEntity user = new UserEntity();
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setUsername("testuser_" + UUID.randomUUID().toString().substring(0, 8));
        user.setEmail("test_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        user.setPassword("encodedPassword");
        user.setBirthDate(LocalDate.now().minusYears(25));
        user.setInscriptionDate(LocalDate.now());
        user.setRoles(new HashSet<>()); // Empty - roles should be added in test with persisted roles
        return user;
    }

    /**
     * Builds a UserEntity with specified email.
     *
     * @param email the email to set
     * @return new UserEntity instance
     */
    public static UserEntity buildUserWithEmail(String email) {
        UserEntity user = buildUser();
        user.setEmail(email);
        return user;
    }

    /**
     * Builds a UserEntity with specified roles.
     *
     * @param roles the Set of RoleEntity to assign (should be persisted entities)
     * @return new UserEntity instance with roles
     */
    public static UserEntity buildUserWithRoles(Set<RoleEntity> roles) {
        UserEntity user = buildUser();
        user.setRoles(roles != null ? roles : new HashSet<>());
        return user;
    }

    /**
     * Builds a basic ProductEntity suitable for creation.
     *
     * @return new ProductEntity instance without ID
     */
    public static ProductEntity buildProduct() {
        ProductEntity product = new ProductEntity();
        product.setName("TestProduct");
        product.setIngredientType(IngredientType.VEGETABLE);
        return product;
    }

    /**
     * Builds a ProductEntity with specified name.
     *
     * @param name the name to set
     * @return new ProductEntity instance
     */
    public static ProductEntity buildProductWithName(String name) {
        ProductEntity product = new ProductEntity();
        product.setName(name);
        product.setIngredientType(IngredientType.VEGETABLE);
        return product;
    }

    /**
     * Builds a list of ProductEntity instances.
     *
     * @param count the number of products to create
     * @return List of ProductEntity instances
     */
    public static List<ProductEntity> buildProductList(int count) {
        return LongStream.rangeClosed(1, Math.min(count, IngredientType.values().length))
                .mapToObj(num -> {
                    ProductEntity product = new ProductEntity();
                    product.setName("Product" + num);
                    product.setIngredientType(IngredientType.getByIndex((int) (num - 1)));
                    return product;
                })
                .toList();
    }

    /**
     * Builds a basic RecipeEntity suitable for creation.
     * Note: This recipe without author is for scenarios where author will be set separately.
     * For normal use in tests, prefer buildRecipeWithTestAuthor() which includes a test author.
     *
     * @return new RecipeEntity instance without ID and without author (for advanced scenarios)
     */
    public static RecipeEntity buildRecipe() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setTitle("TestRecipe_" + UUID.randomUUID().toString().substring(0, 8));
        recipe.setDescription("Test recipe description");
        recipe.setRecipeType(RecipeType.APPETIZER);
        recipe.setPreparationTime(30);
        recipe.setPublicationDate(LocalDate.now());
        return recipe;
    }

    /**
     * Builds and persists a recipe together with a test author using the provided TestEntityManager.
     * Ensures the required author FK is satisfied for tests that persist recipes in one step.
     */
    public static RecipeEntity buildAndPersistRecipeWithTestAuthor(TestEntityManager em) {
        UserEntity author = buildUser();
        em.persistAndFlush(author);

        RecipeEntity recipe = buildRecipe();
        recipe.setAuthor(author);
        return em.persistAndFlush(recipe);
    }

    /**
     * Persist an ingredient and its transient dependencies (product, recipe.author) if needed.
     * Returns the managed IngredientEntity.
     */
    public static IngredientEntity persistIngredientIfNeeded(TestEntityManager em, IngredientEntity ingredient) {
        if (ingredient == null) return null;

        if (ingredient.getProduct() != null && ingredient.getProduct().getId() == null) {
            em.persistAndFlush(ingredient.getProduct());
        }

        if (ingredient.getRecipe() != null && ingredient.getRecipe().getId() == null) {
            RecipeEntity recipe = ingredient.getRecipe();
            if (recipe.getAuthor() != null && recipe.getAuthor().getId() == null) {
                em.persistAndFlush(recipe.getAuthor());
            }
            em.persistAndFlush(recipe);
        }

        return em.persistAndFlush(ingredient);
    }

    /**
     * Builds and persists a recipe with ingredients whose products are persisted first.
     * This avoids transient product issues when persisting recipes with cascading to ingredients.
     *
     * @param em              the TestEntityManager to persist dependent entities
     * @param author          the persisted author to assign
     * @param ingredientCount number of ingredients to create
     * @return persisted RecipeEntity with persisted ingredients and products
     */
    public static RecipeEntity buildAndPersistRecipeWithIngredients(TestEntityManager em, UserEntity author, int ingredientCount) {
        RecipeEntity recipe = buildRecipe();
        recipe.setAuthor(author);

        List<IngredientEntity> ingredients = new ArrayList<>();
        for (int i = 1; i <= Math.min(ingredientCount, Unit.values().length); i++) {
            ProductEntity product = new ProductEntity();
            product.setName("Ingredient" + i);
            product.setIngredientType(IngredientType.VEGETABLE);
            em.persistAndFlush(product);

            IngredientEntity ingredient = new IngredientEntity();
            ingredient.setQuantity((double) i * 10);
            ingredient.setUnit(Unit.getByIndex(i - 1));
            ingredient.setProduct(product);
            ingredient.setRecipe(recipe);
            ingredients.add(ingredient);
        }
        recipe.setIngredientList(ingredients);
        return em.persistAndFlush(recipe);
    }

    /**
     * Builds a RecipeEntity with steps.
     * Note: When persisting, set the recipe's author BEFORE flushing.
     *
     * @param stepCount the number of steps to include
     * @return new RecipeEntity with steps but no author (must be set before persistence)
     */
    public static RecipeEntity buildRecipeWithSteps(int stepCount) {
        RecipeEntity recipe = buildRecipe();
        List<StepEntity> steps = new ArrayList<>();
        for (int i = 1; i <= stepCount; i++) {
            StepEntity step = new StepEntity();
            step.setDescription("Step " + i);
            step.setOrder(i);
            step.setRecipe(recipe);
            steps.add(step);
        }
        recipe.setStepList(steps);
        return recipe;
    }

    /**
     * Builds a basic StepEntity suitable for creation.
     *
     * @return new StepEntity instance without ID
     */
    public static StepEntity buildStep() {
        StepEntity step = new StepEntity();
        step.setDescription("Test step");
        step.setOrder(1);
        return step;
    }

    /**
     * Builds a list of StepEntity instances.
     *
     * @param count the number of steps to create
     * @return List of StepEntity instances
     */
    public static List<StepEntity> buildStepList(int count) {
        List<StepEntity> steps = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            StepEntity step = new StepEntity();
            step.setDescription("Step " + i);
            step.setOrder(i);
            steps.add(step);
        }
        return steps;
    }

    /**
     * Builds a basic IngredientEntity suitable for creation.
     *
     * @return new IngredientEntity instance without ID
     */
    public static IngredientEntity buildIngredient() {
        IngredientEntity ingredient = new IngredientEntity();
        ingredient.setQuantity(10.0);
        ingredient.setUnit(Unit.GRAM);
        ProductEntity product = new ProductEntity();
        product.setName("TestProduct");
        product.setIngredientType(IngredientType.VEGETABLE);
        ingredient.setProduct(product);
        return ingredient;
    }

    /**
     * Builds a list of IngredientEntity instances.
     *
     * @param count the number of ingredients to create
     * @return List of IngredientEntity instances
     */
    public static List<IngredientEntity> buildIngredientList(int count) {
        List<IngredientEntity> ingredients = new ArrayList<>();
        for (int i = 1; i <= Math.min(count, Unit.values().length); i++) {
            IngredientEntity ingredient = new IngredientEntity();
            ingredient.setQuantity((double) i * 10);
            ingredient.setUnit(Unit.getByIndex(i - 1));
            ProductEntity product = new ProductEntity();
            product.setName("Product" + i);
            product.setIngredientType(IngredientType.VEGETABLE);
            ingredient.setProduct(product);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    // ==================== Dataset Builders ====================

    /**
     * Builds a dataset of UserEntity instances with variations.
     *
     * @param count the number of users to create
     * @return List of UserEntity instances with different attributes
     */
    public static List<UserEntity> buildUserDataSet(int count) {
        List<UserEntity> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            UserEntity user = new UserEntity();
            user.setFirstName("User" + i);
            user.setLastName("Last" + i);
            user.setUsername("user" + i + "_" + UUID.randomUUID().toString().substring(0, 6));
            user.setEmail("user" + i + "_" + UUID.randomUUID().toString().substring(0, 6) + "@example.com");
            user.setPassword("encodedPassword");
            user.setBirthDate(LocalDate.now().minusYears(20 + i));
            user.setInscriptionDate(LocalDate.now().minusDays(i));
            user.setRoles(new HashSet<>()); // Empty - roles should be added in test with persisted roles
            users.add(user);
        }
        return users;
    }


    /**
     * Builds a dataset of RecipeEntity instances associated with an author.
     *
     * @param author the UserEntity author
     * @param count  the number of recipes to create
     * @return List of RecipeEntity instances
     */
    public static List<RecipeEntity> buildRecipeDataSet(UserEntity author, int count) {
        List<RecipeEntity> recipes = new ArrayList<>();
        int recipeTypeCount = Math.min(count, RecipeType.values().length);
        for (int i = 0; i < recipeTypeCount; i++) {
            RecipeEntity recipe = new RecipeEntity();
            recipe.setTitle("Recipe" + (i + 1));
            recipe.setDescription("Description for recipe " + (i + 1));
            recipe.setRecipeType(RecipeType.values()[i]);
            recipe.setPreparationTime((i + 1) * 15);
            recipe.setPublicationDate(LocalDate.now().minusDays(i));
            recipe.setAuthor(author);
            recipes.add(recipe);
        }
        return recipes;
    }

}

