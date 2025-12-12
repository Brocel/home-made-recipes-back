package com.example.hmrback.utils.test;

import com.example.hmrback.model.*;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.RecipeType;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.persistence.enums.Unit;
import com.example.hmrback.utils.DateUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.LongStream;

import static com.example.hmrback.utils.test.TestConstants.*;

public class ModelTestUtils {

    private ModelTestUtils() {
    }

    /**
     * Build a User object for testing
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>First Name: "fName" + ordinal</li>
     *     <li>Last Name: "lName" + ordinal</li>
     *     <li>Email: "username" + ordinal + "@test.com"</li>
     *     <li>Birth Date: Current date minus (10 * ordinal) years</li>
     *     <li>Inscription Date: Current date minus ordinal months</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate users
     * @return the User object
     */
    public static User buildUser(Long ordinal) {
        return new User(CommonTestUtils.uuidFromLong(ordinal).toString(),
            FIRST_NAME.formatted(ordinal),
            LAST_NAME.formatted(ordinal),
            USERNAME.formatted(ordinal),
            EMAIL.formatted(ordinal),
            DateUtils.formatLocalDate(LocalDate.now().minusYears(10 * ordinal)),
            DateUtils.formatLocalDate(LocalDate.now().minusMonths(ordinal)),
            new HashSet<>(Collections.singleton(buildRole())));
    }

    /**
     * Build a list of User objects for testing
     *
     * @param count the number of users to create
     * @return the list of User objects
     */
    public static List<User> buildUserList(int count) {
        return LongStream.rangeClosed(1L, count).mapToObj(ModelTestUtils::buildUser).toList();
    }

    /**
     * Build a Product object for testing
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>Name: "Product" + ordinal</li>
     *     <li>Ingredient Type: IngredientType corresponding to (ordinal - 1) index</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate products
     * @return the Product object
     */
    public static Product buildProduct(Long ordinal) {
        return new Product(ordinal, PRODUCT_NAME.formatted(ordinal), IngredientType.getByIndex(ordinal.intValue() - 1));
    }

    /**
     * Build a list of Product objects for testing
     *
     * @param count the number of products to create
     * @return the list of Product objects
     */
    public static List<Product> buildProductList(int count) {
        if (count > IngredientType.values().length) {
            count = IngredientType.values().length;
        }
        return LongStream.rangeClosed(1L, count).mapToObj(ModelTestUtils::buildProduct).toList();
    }

    /**
     * Build a Step object for testing
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>Description: "Step description " + ordinal</li>
     *     <li>Duration: ordinal as int</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate steps
     * @return the Step object
     */
    public static Step buildStep(Long ordinal, boolean isCreation) {
        return new Step(isCreation ? null : ordinal, STEP_DESCRIPTION.formatted(ordinal), ordinal.intValue());
    }

    /**
     * Build a list of Step objects for testing
     *
     * @param count the number of steps to create
     * @return the list of Step objects
     */
    public static List<Step> buildStepList(int count, boolean isCreation) {
        return LongStream.rangeClosed(1L, count).mapToObj(num -> ModelTestUtils.buildStep(num, isCreation)).toList();
    }

    /**
     * Build an Ingredient object for testing
     * <ul>
     *     <li>Id: ordinal</li>
     *     <li>Quantity: ordinal * 10 as double</li>
     *     <li>Unit: Unit corresponding to (ordinal - 1) index</li>
     *     <li>Product: Product object built with the same ordinal</li>
     * </ul>
     *
     * @param ordinal the ordinal to differentiate ingredients
     * @return the Ingredient object
     */
    public static Ingredient buildIngredient(Long ordinal, boolean isCreation) {
        return new Ingredient(isCreation ? null : ordinal,
            ordinal.doubleValue() * 10,
            Unit.getByIndex(ordinal.intValue() - 1),
            buildProduct(ordinal));
    }

    /**
     * Build a list of Ingredient objects for testing
     *
     * @param count the number of ingredients to create
     * @return the list of Ingredient objects
     */
    public static List<Ingredient> buildIngredientList(int count, boolean isCreation) {
        if (count > Unit.values().length) {
            count = Unit.values().length;
        }
        return LongStream.rangeClosed(1L, count).mapToObj(num -> ModelTestUtils.buildIngredient(num, isCreation)).toList();
    }

    public static Recipe buildRecipe(Long ordinal, boolean isCreation) {
        return new Recipe(isCreation ? null : ordinal,
            RECIPE_TITLE.formatted(ordinal),
            RECIPE_DESCRIPTION.formatted(ordinal),
            ordinal.intValue() * 40,
            RecipeType.getByIndex(ordinal.intValue() - 1),
            DateUtils.formatLocalDate(LocalDate.now().minusDays(10 * ordinal)),
            buildUser(ordinal),
            buildIngredientList(4, isCreation),
            buildStepList(5, isCreation));
    }

    public static Recipe buildRecipeForCreation(Long ordinal) {
        return new Recipe(null,
            RECIPE_TITLE.formatted(ordinal),
            RECIPE_DESCRIPTION.formatted(ordinal),
            ordinal.intValue() * 40,
            RecipeType.APPETIZER,
            DateUtils.formatLocalDate(LocalDate.now().minusDays(10 * ordinal)),
            null,
            buildIngredientList(4, true),
            buildStepList(5, true));
    }

    public static Role buildRole() {
        return new Role(1L, RoleEnum.ROLE_USER.toString());
    }

    public static Product buildProductForCreation(boolean existingProduct) {
        return new Product(null,
            existingProduct ? "Carrot" : FAKE,
            existingProduct ? IngredientType.VEGETABLE : IngredientType.OTHER);
    }
}
