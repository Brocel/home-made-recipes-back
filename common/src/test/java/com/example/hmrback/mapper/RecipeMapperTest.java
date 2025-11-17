package com.example.hmrback.mapper;

import com.example.hmrback.BaseTU;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.example.hmrback.mapper.utils.DateMapperImpl;
import com.example.hmrback.model.Recipe;
import com.example.hmrback.persistence.entity.RecipeEntity;
import com.example.hmrback.persistence.enums.RecipeType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.hmrback.utils.test.TestConstants.NUMBER_1;
import static com.example.hmrback.utils.test.TestConstants.RECIPE_DESCRIPTION;
import static com.example.hmrback.utils.test.TestConstants.RECIPE_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {
    RecipeMapperImpl.class,
    UserMapperImpl.class,
    IngredientMapperImpl.class,
    ProductMapperImpl.class,
    StepMapperImpl.class,
    DateMapperImpl.class,
    RoleMapperImpl.class
})
class RecipeMapperTest extends BaseTU {

    @Autowired
    private RecipeMapper mapper;

    private static Recipe model;
    private static RecipeEntity entity;

    @BeforeAll
    static void setUp() {
        // Initialize test data for Recipe and RecipeEntity
        model = ModelTestUtils.buildRecipe(NUMBER_1, false);

        entity = EntityTestUtils.buildRecipeEntity(NUMBER_1, false);
        entity.setIngredientList(EntityTestUtils.buildIngredientEntityList(5, false));
        entity.setStepList(EntityTestUtils.buildStepEntityList(5, false));
        entity.setAuthor(EntityTestUtils.buildUserEntity(NUMBER_1, false));
    }

    @Test
    @Order(1)
    void shouldMapEntityToModel() {
        Recipe result = mapper.toModel(entity);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.id());
        assertEquals(RECIPE_TITLE.formatted(NUMBER_1), result.title());
        assertEquals(RECIPE_DESCRIPTION.formatted(NUMBER_1), result.description());
        assertEquals(RecipeType.STARTER, result.recipeType());
        assertNotNull(result.author());
        assertNotNull(result.ingredientList());
        assertFalse(result.ingredientList().isEmpty());
        assertNotNull(result.stepList());
        assertFalse(result.stepList().isEmpty());
        assertEquals(40, result.preparationTime());
    }

    @Test
    @Order(2)
    void shouldMapModelToEntity() {
        RecipeEntity result = mapper.toEntity(model);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.getId());
        assertEquals(RECIPE_TITLE.formatted(NUMBER_1), result.getTitle());
        assertEquals(RECIPE_DESCRIPTION.formatted(NUMBER_1), result.getDescription());
        assertEquals(RecipeType.STARTER, result.getRecipeType());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getIngredientList());
        assertFalse(result.getIngredientList().isEmpty());
        assertNotNull(result.getStepList());
        assertFalse(result.getStepList().isEmpty());
        assertEquals(40, result.getPreparationTime());
    }
}
