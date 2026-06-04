package com.example.hmrback.model.request;

import com.example.hmrback.constant.DtoContants;
import com.example.hmrback.persistence.enums.IngredientType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(
        @NotBlank
        String name,

        @JsonProperty(DtoContants.INGREDIENT_TYPE)
        @NotNull
        IngredientType ingredientType
) {}