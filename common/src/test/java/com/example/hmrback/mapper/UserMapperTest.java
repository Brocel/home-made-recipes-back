package com.example.hmrback.mapper;

import com.example.hmrback.BaseTU;
import com.example.hmrback.mapper.utils.DateMapperImpl;
import com.example.hmrback.model.User;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.utils.test.EntityTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.hmrback.utils.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {
    UserMapperImpl.class,
    DateMapperImpl.class,
    RoleMapperImpl.class
})
class UserMapperTest extends BaseTU {

    @Autowired
    private UserMapper mapper;

    private static User model;
    private static UserEntity entity;

    @BeforeAll
    static void setUp() {
        // UserEntity
        entity = EntityTestUtils.buildUserEntity(NUMBER_1, false);
        // User
        model = ModelTestUtils.buildUser(NUMBER_1);
    }

    @Test
    @Order(1)
    void shouldMapEntityToModel() {
        User result = mapper.toModel(entity);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.id());
        assertEquals(FIRST_NAME.formatted(NUMBER_1), result.firstName());
        assertEquals(LAST_NAME.formatted(NUMBER_1), result.lastName());
        assertEquals(USERNAME.formatted(NUMBER_1), result.username());
        assertEquals(EMAIL.formatted(NUMBER_1), result.email());
    }

    @Test
    @Order(2)
    void shouldMapModelToEntity() {
        UserEntity result = mapper.toEntity(model);

        assertNotNull(result);
        assertEquals(NUMBER_1, result.getId());
        assertEquals(FIRST_NAME.formatted(NUMBER_1), result.getFirstName());
        assertEquals(LAST_NAME.formatted(NUMBER_1), result.getLastName());
        assertEquals(USERNAME.formatted(NUMBER_1), result.getUsername());
        assertEquals(EMAIL.formatted(NUMBER_1), result.getEmail());
    }
}
