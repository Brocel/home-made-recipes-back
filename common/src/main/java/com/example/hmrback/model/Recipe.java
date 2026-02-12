package com.example.hmrback.model;

import com.example.hmrback.persistence.enums.RecipeType;
import com.example.hmrback.validation.ValidDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

import static com.example.hmrback.constant.ValidationConstants.CANNOT_BE_EMPTY;
import static com.example.hmrback.constant.ValidationConstants.IS_REQUIRED;
import static com.example.hmrback.constant.ValidationConstants.MUST_BE_POSITIVE;

public record Recipe(
    Long id,

    @NotBlank(message = "Title" + IS_REQUIRED)
    String title,

    @NotBlank(message = "Description" + IS_REQUIRED)
    String description,

    @JsonProperty("preparation_time")
    @NotNull(message = "Preparation time" + IS_REQUIRED)
    @Positive(message = "Preparation time" + MUST_BE_POSITIVE)
    Integer preparationTime,

    @JsonProperty("recipe_type")
    @NotNull(message = "Recipe type" + IS_REQUIRED)
    RecipeType recipeType,

    @JsonProperty("publication_date")
    @NotBlank(message = "Publication date" + IS_REQUIRED)
    @ValidDate
    String publicationDate,

    User author,

    @JsonProperty("ingredient_list")
    @NotEmpty(message = "Ingredient list" + CANNOT_BE_EMPTY)
    List<@Valid Ingredient> ingredientList,

    @JsonProperty("step_list")
    @NotEmpty(message = "Step list" + CANNOT_BE_EMPTY)
    List<@Valid Step> stepList) {
}
