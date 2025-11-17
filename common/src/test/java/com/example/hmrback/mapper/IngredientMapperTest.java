package com.example.hmrback.mapper;

import com.example.hmrback.BaseTU;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.example.hmrback.model.Ingredient;
import com.example.hmrback.persistence.entity.IngredientEntity;
import com.example.hmrback.persistence.enums.Unit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.hmrback.utils.test.TestConstants.NUMBER_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {
    IngredientMapperImpl.class,
    ProductMapperImpl.class
})
class IngredientMapperTest extends BaseTU {

    @Autowired
    private IngredientMapper mapper;

    private static Ingredient model;
    private static IngredientEntity entity;

    @BeforeAll
    static void setUp() {
        // UserEntity
        entity = EntityTestUtils.buildIngredientEntity(NUMBER_1, false);
        // User
        model = ModelTestUtils.buildIngredient(NUMBER_1, false);
    }

    @Test
    @Order(1)
    void shouldMapEntityToModel() {
        Ingredient result = mapper.toModel(entity);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.id());
        assertEquals(NUMBER_1.doubleValue() * 10, result.quantity());
        assertEquals(Unit.getByIndex(NUMBER_1.intValue() - 1), result.unit());
        assertNotNull(result.product());
    }

    @Test
    @Order(2)
    void shouldMapModelToEntity() {
        IngredientEntity result = mapper.toEntity(model);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.getId());
        assertEquals(NUMBER_1.doubleValue() * 10, result.getQuantity());
        assertEquals(Unit.getByIndex(NUMBER_1.intValue() - 1), result.getUnit());
        assertNotNull(result.getProduct());
    }
}
