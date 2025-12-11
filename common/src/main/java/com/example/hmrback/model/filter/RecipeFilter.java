package com.example.hmrback.model.filter;

import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.RecipeType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecipeFilter(
    String title,

    String description,

    @JsonProperty("maximum_preparation_time")
    Integer maximumPreparationTime,

    @JsonProperty("recipe_type_list")
    List<RecipeType> recipeTypeList,

    @JsonProperty("author_username")
    String authorUsername,

    @JsonProperty("ingredient_name_list")
    List<String> ingredientNameList,

    @JsonProperty("ingredient_type_list")
    List<IngredientType> ingredientTypeList) {
}