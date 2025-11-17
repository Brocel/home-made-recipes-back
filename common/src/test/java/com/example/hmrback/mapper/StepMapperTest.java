package com.example.hmrback.mapper;

import com.example.hmrback.BaseTU;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.example.hmrback.model.Step;
import com.example.hmrback.persistence.entity.StepEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.hmrback.utils.test.TestConstants.NUMBER_1;
import static com.example.hmrback.utils.test.TestConstants.STEP_DESCRIPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = { StepMapperImpl.class })
class StepMapperTest extends BaseTU {

    @Autowired
    private StepMapper mapper;

    private static Step model;
    private static StepEntity entity;

    @BeforeAll
    static void setUp() {
        // UserEntity
        entity = EntityTestUtils.buildStepEntity(NUMBER_1, false);
        // User
        model = ModelTestUtils.buildStep(NUMBER_1, false);
    }

    @Test
    @Order(1)
    void shouldMapEntityToModel() {
        Step result = mapper.toModel(entity);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.id());
        assertEquals(STEP_DESCRIPTION.formatted(NUMBER_1), result.description());
        assertEquals(NUMBER_1.intValue(), result.order());
    }

    @Test
    @Order(2)
    void shouldMapModelToEntity() {
        StepEntity result = mapper.toEntity(model);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.getId());
        assertEquals(STEP_DESCRIPTION.formatted(NUMBER_1), result.getDescription());
        assertEquals(NUMBER_1.intValue(), result.getOrder());
    }
}
