package com.example.hmrback.mapper;

import com.example.hmrback.BaseTU;
import com.example.hmrback.model.Role;
import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.hmrback.utils.test.TestConstants.NUMBER_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = { RoleMapperImpl.class })
class RoleMapperTest extends BaseTU {

    @Autowired
    private RoleMapper mapper;

    private static Role model;
    private static RoleEntity entity;

    @BeforeAll
    static void setUp() {
        // UserEntity
        entity = EntityTestUtils.buildRoleEntity();
        // User
        model = ModelTestUtils.buildRole();
    }

    @Test
    @Order(1)
    void shouldMapEntityToModel() {
        Role result = mapper.toModel(entity);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.id());
        assertEquals(RoleEnum.ROLE_USER.toString(), result.name());
    }

    @Test
    @Order(2)
    void shouldMapModelToEntity() {
        RoleEntity result = mapper.toEntity(model);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.getId());
        assertEquals(RoleEnum.ROLE_USER, result.getName());
    }
}
