package com.example.hmrback.predicate.factory;

import com.example.hmrback.model.filter.RecipeFilter;
import com.example.hmrback.predicate.builder.RecipePredicateBuilder;
import com.querydsl.core.types.Predicate;

public class RecipePredicateFactory {

    private RecipePredicateFactory() {
    }

    /**
     * Create a Predicate from RecipeFilter
     *
     * @param filters RecipeFilter object containing filter criteria
     * @return Predicate object representing the filter criteria
     */
    public static Predicate fromFilters(RecipeFilter filters) {
        return new RecipePredicateBuilder()
            .titleContains(filters.title())
            .descriptionContains(filters.description())
            .maxPrepTime(filters.maximumPreparationTime())
            .recipeTypeIn(filters.recipeTypeList())
            .authorUsernameContains(filters.authorUsername())
            .ingredientNameIn(filters.ingredientNameList())
            .ingrendientTypeIn(filters.ingredientTypeList())
            .build();
    }
}
