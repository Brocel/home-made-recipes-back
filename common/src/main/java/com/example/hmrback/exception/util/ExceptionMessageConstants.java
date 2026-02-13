package com.example.hmrback.exception.util;

public class ExceptionMessageConstants {

    private ExceptionMessageConstants() {
    }

    public static final String EXCEPTION_BASE_MESSAGE = "HMRGenericException {}";


    // User
    public static final String USER_NOT_FOUND_MESSAGE = "L'utilisateur %s est introuvable.";

    // Recipe
    public static final String RECIPE_NOT_FOUND_EXCEPTION_MESSAGE = "La recette avec l'id %s est introuvable.";

    // Product
    public static final String PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE = "Le produit avec l'id %s est introuvable.";

}
