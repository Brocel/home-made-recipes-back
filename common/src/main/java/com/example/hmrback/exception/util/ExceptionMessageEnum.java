package com.example.hmrback.exception.util;

import lombok.Getter;

@Getter
public enum ExceptionMessageEnum {
    // Common
    RESOURCE_NOT_FOUND("The resource cannot be found", "exception.common.resourceNotFound"),
    ACCESS_DENIED("You don't have permission to perform this action.", "exception.common.accessDenied"),
    // Recipes
    RECIPE_NOT_FOUND_BY_ID("The recipe (id: %s) couldn't be found.", "exception.recipes.notFoundById"),
    RECIPE_NOT_FOUND_BY_NAME("The recipe (name: %s) couldn't be found.", "exception.recipes.notFoundByName"),
    DAILY_RECIPE_NOT_FOUND("Daily recipe couldn't be found", "exception.recipes.dailyNotFound"),
    RECIPE_AUTHOR_ONLY("Only recipe's author can modify this recipe.", "exception.recipes.authorOnly"),
    // Products
    PRODUCT_NOT_FOUND_BY_ID("The product (id: %s) couldn't be found.", "exception.products.notFoundById"),
    PRODUCT_NOT_FOUND_BY_NAME("The product (name: %s) couldn't be found.", "exception.products.notFoundByName"),
    // Planner
    // User
    EMAIL_NOT_FOUND("Email (%s) not found.", "exception.user.emailNotFound"),
    USERNAME_ALREADY_EXISTS("Username (%s) already exists.", "exception.user.usernameAlreadyExists"),
    USER_NOT_FOUND("User (%s) not found.", "exception.user.userNotFound"),
    // Dashboard
    // Auth
    EMAIL_ALREADY_EXISTS("Email (%s) already exists.", "exception.auth.emailAlreadyExists"),
    AUTHENTICATION_REQUIRED("Authentication is required to access this resource.", "exception.auth.required"),
    INVALID_PASSWORD("Password is not valid.", "exception.auth.invalidPassword");


    private final String message;
    private final String errorKey;

    ExceptionMessageEnum(String message, String errorKey) {
        this.message = message;
        this.errorKey = errorKey;
    }
}
