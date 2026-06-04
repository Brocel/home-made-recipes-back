package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.User;
import com.example.hmrback.model.response.UserResponse;
import com.example.hmrback.utils.test.ModelTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.hmrback.utils.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserDTOMapperTest")
class UserDTOMapperTest {

    @Test
    @DisplayName("Should convert valid User to UserResponse with all fields preserved")
    void shouldConvertValidUserToResponse() {
        // Given
        User user = ModelTestUtils.buildUser(1L, false);

        // When
        UserResponse response = UserDTOMapper.toDtoResponse(user);

        // Then
        assertNotNull(response);
        assertEquals(user.id(), response.id());
        assertEquals(user.firstName(), response.firstName());
        assertEquals(user.lastName(), response.lastName());
        assertEquals(user.username(), response.username());
        assertEquals(user.email(), response.email());
        assertEquals(user.birthDate(), response.birthDate());
        assertEquals(user.inscriptionDate(), response.inscriptionDate());
        assertEquals(user.avatarName(), response.avatarName());
        assertEquals(user.roles(), response.roles());
    }

    @Test
    @DisplayName("Should handle null User gracefully")
    void shouldHandleNullUser() {
        // When
        UserResponse response = UserDTOMapper.toDtoResponse(null);

        // Then
        assertNull(response);
    }

    @Test
    @DisplayName("Should preserve id field in conversion")
    void shouldPreserveIdField() {
        // Given
        User user = ModelTestUtils.buildUser(2L, false);

        // When
        UserResponse response = UserDTOMapper.toDtoResponse(user);

        // Then
        assertEquals(user.id(), response.id());
    }

    @Test
    @DisplayName("Should preserve roles in conversion")
    void shouldPreserveRolesInConversion() {
        // Given
        User user = ModelTestUtils.buildUser(3L, false);

        // When
        UserResponse response = UserDTOMapper.toDtoResponse(user);

        // Then
        assertNotNull(response.roles());
        assertFalse(response.roles().isEmpty());
        assertEquals(user.roles(), response.roles());
    }

    @Test
    @DisplayName("Should preserve all user fields when converting to response")
    void shouldPreserveAllUserFieldsInResponse() {
        // Given
        User user = ModelTestUtils.buildUser(NUMBER_1, false);

        // When
        UserResponse response = UserDTOMapper.toDtoResponse(user);

        // Then
        assertNotNull(response);
        assertAll(
                () -> assertEquals(user.id(), response.id(), "ID should be preserved"),
                () -> assertEquals(user.firstName(), response.firstName(), "First name should be preserved"),
                () -> assertEquals(user.lastName(), response.lastName(), "Last name should be preserved"),
                () -> assertEquals(user.username(), response.username(), "Username should be preserved"),
                () -> assertEquals(user.email(), response.email(), "Email should be preserved"),
                () -> assertEquals(user.birthDate(), response.birthDate(), "Birth date should be preserved"),
                () -> assertEquals(user.inscriptionDate(), response.inscriptionDate(), "Inscription date should be preserved"),
                () -> assertEquals(user.avatarName(), response.avatarName(), "Avatar name should be preserved"),
                () -> assertEquals(user.roles(), response.roles(), "Roles should be preserved")
        );
    }
}

