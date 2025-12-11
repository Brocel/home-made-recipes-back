package com.example.hmrback.predicate.factory;

import com.example.hmrback.model.filter.ProductFilter;
import com.example.hmrback.predicate.builder.ProductPredicateBuilder;
import com.querydsl.core.types.Predicate;

public class ProductPredicateFactory {

    private ProductPredicateFactory() {
    }

    /**
     * Create a Predicate from ProductFilter
     *
     * @param filters ProductFilter object containing filter criteria
     * @return Predicate object representing the filter criteria
     */
    public static Predicate fromFilters(ProductFilter filters) {
        return new ProductPredicateBuilder()
            .normalizedNameContains(filters.name())
            .ingredientTypeIn(filters.ingredientTypeList())
            .build();
    }
}
