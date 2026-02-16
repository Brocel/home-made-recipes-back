package com.example.hmrback.exception.util;

import lombok.Getter;

@Getter
public enum ExceptionMessageEnum {
    // Auth
    ACCESS_DENIED("You don't have permission to perform this action."),
    INVALID_PASSWORD("Password is not valid."),
    // User
    EMAIL_ALREADY_EXISTS("Email already exists."),
    EMAIL_NOT_FOUND("Email not found."),
    USERNAME_ALREADY_EXISTS("Username already exists."),
    USER_NOT_FOUND("User '%s' not found."),
    // Recipe
    RECIPE_NOT_FOUND_BY_ID("The recipe (id :: %s) couldn't be found."),
    RECIPE_NOT_FOUND_BY_NAME("The recipe (name :: %s) couldn't be found."),
    // Product
    PRODUCT_NOT_FOUND_BY_ID("The product (id :: %s) couldn't be found."),
    PRODUCT_NOT_FOUND_BY_NAME("The product (name :: %s) couldn't be found.");


    private final String message;

    ExceptionMessageEnum(String message) {
        this.message = message;
    }
}
