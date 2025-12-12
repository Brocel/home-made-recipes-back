package com.example.hmrback.utils.test;

import com.example.hmrback.persistence.entity.IngredientEntity;
import com.example.hmrback.persistence.entity.ProductEntity;
import com.example.hmrback.persistence.entity.RecipeEntity;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.Unit;
import com.example.hmrback.utils.NormalizeUtils;
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
     * No Product in catgeory 'OTHER'
     *
     * @return List of ProductEntity objects.
     */
    public static List<ProductEntity> buildProductEntityList() {
        ProductEntity carrot = new ProductEntity();
        carrot.setName("Carrot");
        carrot.setIngredientType(IngredientType.VEGETABLE);
        carrot.setNormalizedName(NormalizeUtils.normalizeText(carrot.getName()));

        ProductEntity apple = new ProductEntity();
        apple.setName("Apple");
        apple.setIngredientType(IngredientType.FRUIT);
        apple.setNormalizedName(NormalizeUtils.normalizeText(apple.getName()));

        ProductEntity chicken = new ProductEntity();
        chicken.setName("Chicken");
        chicken.setIngredientType(IngredientType.MEAT);
        chicken.setNormalizedName(NormalizeUtils.normalizeText(chicken.getName()));

        ProductEntity fish = new ProductEntity();
        fish.setName("Salmon");
        fish.setIngredientType(IngredientType.FISH);
        fish.setNormalizedName(NormalizeUtils.normalizeText(fish.getName()));

        ProductEntity sf = new ProductEntity();
        sf.setName("Mussels");
        sf.setIngredientType(IngredientType.SEA_FRUIT);
        sf.setNormalizedName(NormalizeUtils.normalizeText(sf.getName()));

        ProductEntity cream = new ProductEntity();
        cream.setName("Cream");
        cream.setIngredientType(IngredientType.DAIRY);
        cream.setNormalizedName(NormalizeUtils.normalizeText(cream.getName()));

        ProductEntity cheese = new ProductEntity();
        cheese.setName("Maroilles");
        cheese.setIngredientType(IngredientType.CHEESE);
        cheese.setNormalizedName(NormalizeUtils.normalizeText(cheese.getName()));

        ProductEntity rice = new ProductEntity();
        rice.setName("Rice");
        rice.setIngredientType(IngredientType.GRAIN);
        rice.setNormalizedName(NormalizeUtils.normalizeText(rice.getName()));

        ProductEntity spice = new ProductEntity();
        spice.setName("Cinnamon");
        spice.setIngredientType(IngredientType.SPICE);
        spice.setNormalizedName(NormalizeUtils.normalizeText(spice.getName()));

        ProductEntity cond = new ProductEntity();
        cond.setName("Pickles");
        cond.setIngredientType(IngredientType.CONDIMENT);
        cond.setNormalizedName(NormalizeUtils.normalizeText(cond.getName()));

        ProductEntity nut = new ProductEntity();
        nut.setName("Haselnut");
        nut.setIngredientType(IngredientType.NUT);
        nut.setNormalizedName(NormalizeUtils.normalizeText(nut.getName()));

        ProductEntity herb = new ProductEntity();
        herb.setName("Thyme");
        herb.setIngredientType(IngredientType.HERBS);
        herb.setNormalizedName(NormalizeUtils.normalizeText(herb.getName()));

        ProductEntity fat = new ProductEntity();
        fat.setName("Cinnamon");
        fat.setIngredientType(IngredientType.FAT);
        fat.setNormalizedName(NormalizeUtils.normalizeText(fat.getName()));

        ProductEntity sugar = new ProductEntity();
        sugar.setName("Brown sugar");
        sugar.setIngredientType(IngredientType.SUGAR);
        sugar.setNormalizedName(NormalizeUtils.normalizeText(sugar.getName()));

        ProductEntity water = new ProductEntity();
        water.setName("Water");
        water.setIngredientType(IngredientType.BEVERAGE);
        water.setNormalizedName(NormalizeUtils.normalizeText(water.getName()));

        ProductEntity drink = new ProductEntity();
        drink.setName("Red Red Wine");
        drink.setIngredientType(IngredientType.ALCOHOL);
        drink.setNormalizedName(NormalizeUtils.normalizeText(drink.getName()));

        ProductEntity starches = new ProductEntity();
        starches.setName("Potato");
        starches.setIngredientType(IngredientType.STARCHES);
        starches.setNormalizedName(NormalizeUtils.normalizeText(starches.getName()));

        return List.of(carrot, apple, chicken, fish, sf, cream, cheese, rice, spice, cond, nut, herb, fat, sugar, water, drink, starches);
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
