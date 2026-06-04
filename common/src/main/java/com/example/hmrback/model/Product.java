package com.example.hmrback.model;

import com.example.hmrback.persistence.enums.IngredientType;

public record Product(
        Long id,

        String name,

        IngredientType ingredientType
) {
}
