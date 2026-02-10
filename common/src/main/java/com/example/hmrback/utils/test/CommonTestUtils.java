package com.example.hmrback.utils.test;

import com.example.hmrback.model.filter.ProductFilter;
import com.example.hmrback.model.filter.RecipeFilter;
import com.example.hmrback.model.request.LoginRequest;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.RecipeType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.hmrback.utils.test.TestConstants.*;

public class CommonTestUtils {

    private CommonTestUtils() {
    }

    public static RecipeFilter buildRecipeFilter(RecipeFilterEnum filterEnum, boolean matchingFilters) {
        String title = matchingFilters ? "Recipe" : FAKE;
        String description = matchingFilters ? "Description" : FAKE;
        int maxPrepTime = matchingFilters ? 30 : 1;
        List<RecipeType> recipeTypes = matchingFilters ? List.of(RecipeType.APPETIZER) : List.of(RecipeType.SIDE_DISH);
        String userName = matchingFilters ? "otherUser" : FAKE;
        List<String> ingredientNames = matchingFilters ? List.of("Apple", "Carrot") : List.of(FAKE);
        List<IngredientType> ingredientTypes = matchingFilters ? List.of(IngredientType.VEGETABLE) : List.of(IngredientType.OTHER);

        return new RecipeFilter(RecipeFilterEnum.TITLE.equals(filterEnum) ? title : null,
            RecipeFilterEnum.DESCRIPTION.equals(filterEnum) ? description : null,
            RecipeFilterEnum.MAXIMUM_PREPARATION_TIME.equals(filterEnum) ? maxPrepTime : null,
            RecipeFilterEnum.RECIPE_TYPE.equals(filterEnum) ? recipeTypes : null,
            RecipeFilterEnum.AUTHOR_USERNAME.equals(filterEnum) ? userName : null,
            RecipeFilterEnum.INGREDIENT_NAME.equals(filterEnum) ? ingredientNames : null,
            RecipeFilterEnum.INGREDIENT_TYPE.equals(filterEnum) ? ingredientTypes : null);
    }

    public static LoginRequest buildRegisterRequest() {
        return new LoginRequest("idToken");
    }

    public static UUID uuidFromLong(long value) {
        long mostSigBits = 0x123456789ABCDEFL;   // arbitrary but constant
        return new UUID(mostSigBits, value);
    }

    public static ProductFilter buildProductFilter(ProductFilterEnum productFilterEnum, boolean matchingFilters) {
        String productName = matchingFilters ? "Carrot" : FAKE;

        List<IngredientType> ingredientTypes = switch (productFilterEnum) {
            case ALL_TYPE -> List.of(IngredientType.values());
            case ONLY_OTHER -> List.of(IngredientType.OTHER);
            case SINGLE_CATEGORY -> List.of(IngredientType.VEGETABLE);
            case THREE_CATEGORIES -> List.of(IngredientType.VEGETABLE, IngredientType.FRUIT, IngredientType.MEAT);
            case null, default -> null;
        };

        if (!matchingFilters) {
            ingredientTypes = new ArrayList<>();
            ingredientTypes.add(IngredientType.OTHER);
        }

        return new ProductFilter(ProductFilterEnum.JUST_NAME.equals(productFilterEnum) ? productName : null,
            ProductFilterEnum.NULL.equals(productFilterEnum) ? null : ingredientTypes);
    }
}
