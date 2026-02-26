package com.example.hmrback.utils;

import com.example.hmrback.model.request.UserUpdateRequest;
import com.example.hmrback.persistence.entity.UserEntity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class UserUtils {

    private UserUtils() {
    }

    public static Set<String> getCommaSeparatedEmails(String raw) {
        return Arrays.stream(raw.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .map(String::toLowerCase)
                     .collect(Collectors.toSet());
    }

    /**
     * Updates a user entity
     *
     * @param user old version of user
     * @param req  update request
     * @return the updated user
     */
    public static UserEntity updateUser(UserEntity user,
                                        UserUpdateRequest req) {

        if (user != null && req != null) {
            if (req.firstName() != null) {
                user.setFirstName(req.firstName());
            }
            if (req.lastName() != null) {
                user.setLastName(req.lastName());
            }
            if (req.username() != null) {
                user.setUsername(req.username());
            }
            if (req.birthDate() != null) {
                user.setBirthDate(DateUtils.parseLocalDate(req.birthDate()));
            }
            if (req.avatarName() != null) {
                user.setAvatarName(req.avatarName());
            }
        }

        return user;
    }

}
