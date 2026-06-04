package com.example.hmrback.model;

import com.example.hmrback.persistence.enums.RecipeType;

import java.util.List;


public record Recipe(
        Long id,

        String title,

        String description,

        Integer preparationTime,

        RecipeType recipeType,

        String publicationDate,

        User author,

        List<Ingredient> ingredientList,

        List<Step> stepList) {
}
