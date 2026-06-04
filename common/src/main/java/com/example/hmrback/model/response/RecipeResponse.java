package com.example.hmrback.model.response;

import com.example.hmrback.constant.DtoContants;
import com.example.hmrback.model.Ingredient;
import com.example.hmrback.model.Step;
import com.example.hmrback.persistence.enums.RecipeType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecipeResponse(
        Long id,

        String title,

        String description,

        @JsonProperty(DtoContants.PREPARATION_TIME)
        Integer preparationTime,

        @JsonProperty(DtoContants.RECIPE_TYPE)
        RecipeType recipeType,

        @JsonProperty(DtoContants.PUBLICATION_DATE)
        String publicationDate,

        UserResponse author,

        @JsonProperty(DtoContants.INGREDIENT_LIST)
        List<Ingredient> ingredientList,

        @JsonProperty(DtoContants.STEP_LIST)
        List<Step> stepList
) {}