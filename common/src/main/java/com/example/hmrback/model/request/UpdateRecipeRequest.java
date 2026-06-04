package com.example.hmrback.model.request;

import com.example.hmrback.constant.DtoContants;
import com.example.hmrback.persistence.enums.RecipeType;
import com.example.hmrback.validation.ValidDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record UpdateRecipeRequest(
        @NotBlank
        String title,

        @NotBlank
        String description,

        @JsonProperty(DtoContants.PREPARATION_TIME)
        @NotNull
        @Positive
        Integer preparationTime,

        @JsonProperty(DtoContants.RECIPE_TYPE)
        @NotNull
        RecipeType recipeType,

        @JsonProperty(DtoContants.PUBLICATION_DATE)
        @NotBlank
        @ValidDate
        String publicationDate,

        @JsonProperty(DtoContants.INGREDIENT_LIST)
        @NotEmpty
        List<@Valid UpdateIngredientRequest> ingredientList,

        @JsonProperty(DtoContants.STEP_LIST)
        @NotEmpty
        List<@Valid UpdateStepRequest> stepList
) {
}