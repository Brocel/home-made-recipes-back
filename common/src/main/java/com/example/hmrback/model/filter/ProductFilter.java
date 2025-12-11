package com.example.hmrback.model.filter;

import com.example.hmrback.persistence.enums.IngredientType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProductFilter(
    String name,

    @JsonProperty("ingredient_type_list")
    List<IngredientType> ingredientTypeList) {
}
