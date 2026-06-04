package com.example.hmrback.model.request;

import com.example.hmrback.model.Product;
import com.example.hmrback.persistence.enums.Unit;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateIngredientRequest(
        @NotNull
        @Positive
        Double quantity,

        @NotNull
        Unit unit,

        @NotNull
        Product product
) {
}
