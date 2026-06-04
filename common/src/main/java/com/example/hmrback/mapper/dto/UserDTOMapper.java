package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.User;
import com.example.hmrback.model.response.UserResponse;

/**
 * Maps User domain models to UserResponse DTOs and vice versa.
 *
 * This utility mapper provides stateless, static transformations between
 * the User domain model and the UserResponse DTO. All methods are null-safe
 * and preserve all user information including roles and dates.
 *
 * Usage:
 * <pre>
 *   UserResponse response = UserDTOMapper.toDtoResponse(user);
 * </pre>
 *
 * @since 1.0
 */
public final class UserDTOMapper {

    private UserDTOMapper() {
        // Utility class, no instantiation
    }

    /**
     * Converts a User domain model to a UserResponse DTO.
     *
     * All fields are preserved: id, firstName, lastName, username, email,
     * birthDate, inscriptionDate, avatarName, and roles.
     *
     * @param user the User domain model to convert (may be null)
     * @return UserResponse DTO, or null if input is null
     */
    public static UserResponse toDtoResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.id(),
                user.firstName(),
                user.lastName(),
                user.username(),
                user.email(),
                user.birthDate(),
                user.inscriptionDate(),
                user.avatarName(),
                user.roles()
        );
    }
}

