package com.example.hmrback.utils.test;

import com.example.hmrback.persistence.entity.IngredientEntity;
import com.example.hmrback.persistence.entity.ProductEntity;
import com.example.hmrback.persistence.entity.RecipeEntity;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.Unit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

public class IntegrationTestUtils {

    private IntegrationTestUtils() {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    // Setup base entities in DB for integration tests

    /**
     * Builds a list of ProductEntity with various IngredientTypes for testing purposes.
     *
     * @return List of ProductEntity objects.
     */
    public static List<ProductEntity> buildProductEntityList() {
        ProductEntity carrot = new ProductEntity();
        carrot.setName("Carrot");
        carrot.setIngredientType(IngredientType.VEGETABLE);

        ProductEntity apple = new ProductEntity();
        apple.setName("Apple");
        apple.setIngredientType(IngredientType.FRUIT);

        ProductEntity chicken = new ProductEntity();
        chicken.setName("Chicken");
        chicken.setIngredientType(IngredientType.MEAT);

        ProductEntity fish = new ProductEntity();
        fish.setName("Salmon");
        fish.setIngredientType(IngredientType.FISH);

        ProductEntity cream = new ProductEntity();
        cream.setName("Cream");
        cream.setIngredientType(IngredientType.DAIRY);

        ProductEntity rice = new ProductEntity();
        rice.setName("Rice");
        rice.setIngredientType(IngredientType.GRAIN);

        ProductEntity spice = new ProductEntity();
        spice.setName("Cinnamon");
        spice.setIngredientType(IngredientType.SPICE);

        return List.of(carrot, apple, chicken, fish, cream, rice, spice);
    }

    public static List<IngredientEntity> buildIngredientEntityList(RecipeEntity recipe, List<ProductEntity> products) {
        return products.stream()
            .map(p -> {
                IngredientEntity ing = new IngredientEntity();
                ing.setRecipe(recipe);
                ing.setProduct(p);
                ing.setQuantity(100D);
                ing.setUnit(Unit.GRAM);
                return ing;
            })
            .toList();
    }
}
