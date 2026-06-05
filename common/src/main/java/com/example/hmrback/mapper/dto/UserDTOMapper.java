package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.User;
import com.example.hmrback.model.request.UpdateUserRequest;
import com.example.hmrback.model.response.UserResponse;

/**
 * Maps User domain models to UserResponse DTOs and vice versa.
 * <p>
 * This utility mapper provides stateless, static transformations between
 * the User domain model and the UserResponse DTO. All methods are null-safe
 * and preserve all user information including roles and dates.
 * <p>
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
     * <p>
     * All fields are preserved: id, firstName, lastName, username, email,
     * birthDate, inscriptionDate, avatar, and roles.
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
                user.avatar(),
                user.roles()
        );
    }

    /**
     * Converts an UpdateUserRequest to a User domain model.
     * <p>
     * Note: in the returned model, null fields are not updatable (see UserMapper)
     *
     * @param request the UpdateUserRequest to convert (may be null)
     * @return User domain model
     */
    public static User toUser(UpdateUserRequest request) {
        if (request == null) {
            return null;
        }

        return new User(
                null,
                request.firstName(),
                request.lastName(),
                null,
                null,
                request.birthDate(),
                null,
                request.avatar(),
                null
        );
    }
}

