package com.example.hmrback.validation.util;

import lombok.Getter;

@Getter
public enum ValidationMessageEnum {
    // Recipe validations
    RECIPE_TITLE_REQUIRED(
            "Recipe 'title' is required (3-250 characters)",
            "form.validation.recipe.titleRequired",
            "3-250"),
    RECIPE_DESCRIPTION_REQUIRED(
            "Recipe 'description' is required",
            "form.validation.recipe.descriptionRequired",
            ""),
    RECIPE_PREPARATION_TIME_REQUIRED(
            "Recipe 'preparation_time' is required (must be positive)",
            "form.validation.recipe.preparationTimeRequired",
            ""),
    RECIPE_PREPARATION_TIME_POSITIVE(
            "Recipe 'preparation_time' must be positive",
            "form.validation.recipe.preparationTimePositive",
            ""),
    RECIPE_TYPE_REQUIRED(
            "Recipe 'recipe_type' is required",
            "form.validation.recipe.typeRequired",
            ""),
    RECIPE_PUBLICATION_DATE_REQUIRED(
            "Recipe 'publication_date' is required",
            "form.validation.recipe.publicationDateRequired",
            ""),
    RECIPE_PUBLICATION_DATE_INVALID(
            "Recipe 'publication_date' must be a valid date (dd/MM/yyyy)",
            "form.validation.recipe.publicationDateInvalid",
            "dd/MM/yyyy"),
    RECIPE_INGREDIENTS_EMPTY(
            "Recipe 'ingredient_list' cannot be empty",
            "form.validation.recipe.ingredientsEmpty",
            ""),
    RECIPE_STEPS_EMPTY(
            "Recipe 'step_list' cannot be empty",
            "form.validation.recipe.stepsEmpty",
            ""),

    // Product validations
    PRODUCT_NAME_REQUIRED(
            "Product 'name' is required (3-100 characters)",
            "form.validation.product.nameRequired",
            "3-100"),
    PRODUCT_TYPE_REQUIRED(
            "Product 'ingredient_type' is required",
            "form.validation.product.typeRequired",
            ""),

    // User validations
    USER_FIRST_NAME_REQUIRED(
            "User 'first_name' is required",
            "form.validation.user.firstNameRequired",
            ""),
    USER_LAST_NAME_REQUIRED(
            "User 'last_name' is required",
            "form.validation.user.lastNameRequired",
            ""),
    USER_USERNAME_REQUIRED(
            "User 'username' is required",
            "form.validation.user.usernameRequired",
            ""),
    USER_EMAIL_REQUIRED(
            "User 'email' is required",
            "form.validation.user.emailRequired",
            ""),
    USER_EMAIL_INVALID(
            "User 'email' must be a valid email address",
            "form.validation.user.emailInvalid",
            ""),
    USER_BIRTH_DATE_REQUIRED(
            "User 'birth_date' is required",
            "form.validation.user.birthDateRequired",
            ""),
    USER_BIRTH_DATE_INVALID(
            "User 'birth_date' must be a valid date (dd/MM/yyyy)",
            "form.validation.user.birthDateInvalid",
            "dd/MM/yyyy"),
    USER_PASSWORD_REQUIRED(
            "User 'password' is required (8-128 characters)",
            "form.validation.user.passwordRequired",
            "8-128"),

    // Ingredient validations
    INGREDIENT_NAME_REQUIRED(
            "Ingredient 'name' is required",
            "form.validation.ingredient.nameRequired",
            ""),
    INGREDIENT_TYPE_REQUIRED(
            "Ingredient 'ingredient_type' is required",
            "form.validation.ingredient.typeRequired",
            ""),

    // Step validations
    STEP_DESCRIPTION_REQUIRED(
            "Step 'description' is required",
            "form.validation.step.descriptionRequired",
            ""),

    // Login/Register validations
    LOGIN_EMAIL_REQUIRED(
            "Login 'email' is required",
            "form.validation.auth.emailRequired",
            ""),
    LOGIN_PASSWORD_REQUIRED(
            "Login 'password' is required",
            "form.validation.auth.passwordRequired",
            ""),
    REGISTER_FIRST_NAME_REQUIRED(
            "Registration 'first_name' is required",
            "form.validation.auth.firstNameRequired",
            ""),
    REGISTER_LAST_NAME_REQUIRED(
            "Registration 'last_name' is required",
            "form.validation.auth.lastNameRequired",
            ""),
    REGISTER_USERNAME_REQUIRED(
            "Registration 'username' is required",
            "form.validation.auth.usernameRequired",
            ""),
    REGISTER_EMAIL_REQUIRED(
            "Registration 'email' is required",
            "form.validation.auth.emailRequired",
            ""),
    REGISTER_PASSWORD_REQUIRED(
            "Registration 'password' is required (8-128 characters)",
            "form.validation.auth.passwordRequired",
            "8-128"),
    REGISTER_BIRTH_DATE_REQUIRED(
            "Registration 'birth_date' is required",
            "form.validation.auth.birthDateRequired",
            ""),
    // Defaults
    FIELD_REQUIRED(
            "Field is required",
            "form.validation.default.fieldRequired",
            ""),
    FIELD_POSITIVE(
            "Field should be positive",
            "form.validation.default.fieldPositive",
            ""),
    FIELD_INVALID(
            "Field is invalid",
            "form.validation.default.fieldInvalid",
            ""),
    DATE_INVALID(
            "Date is invalid",
            "form.validation.default.dateInvalid",
            "dd/MM/yyyy"),
    EMAIL_INVALID(
            "Email is invalid",
            "form.validation.default.emailInvalid",
            "")
    ;

    private final String message;
    private final String errorKey;
    private final String messageArg;

    ValidationMessageEnum(String message, String errorKey, String messageArg) {
        this.message = message;
        this.errorKey = errorKey;
        this.messageArg = messageArg;
    }
}
