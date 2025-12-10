package com.example.hmrback.exception.util;

public class ExceptionMessageConstants {

    private ExceptionMessageConstants() {
    }

    public static final String EXCEPTION_BASE_MESSAGE = "HMRGenericException {}";

    public static final String ACCESS_DENIED_EXCEPTION_MESSAGE = "Vous n'avez pas la permission de réaliser cette action.";
    public static final String ROLE_NOT_FOUND_MESSAGE = "Le rôle '%s' est introuvable.";

    // User
    public static final String USER_EMAIL_ALREADY_EXISTS_MESSAGE = "L'utilisateur avec l'email %s existe déjà.";
    public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "L'utilisateur %s existe déjà.";
    public static final String USER_NOT_FOUND_MESSAGE = "L'utilisateur %s est introuvable.";

    // Recipe
    public static final String RECIPE_NOT_FOUND_EXCEPTION_MESSAGE = "La recette avec l'id %s est introuvable.";

}
