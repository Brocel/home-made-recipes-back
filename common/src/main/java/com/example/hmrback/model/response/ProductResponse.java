package com.example.hmrback.model.response;

import com.example.hmrback.constant.DtoContants;
import com.example.hmrback.persistence.enums.IngredientType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductResponse(
        Long id,

        String name,

        @JsonProperty(DtoContants.INGREDIENT_TYPE)
        IngredientType ingredientType
) {}