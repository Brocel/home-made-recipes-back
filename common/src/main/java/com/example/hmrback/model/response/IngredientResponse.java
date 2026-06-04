package com.example.hmrback.model.response;

import com.example.hmrback.model.Product;
import com.example.hmrback.persistence.enums.Unit;

public record IngredientResponse(
        Long id,

        Double quantity,

        Unit unit,

        Product product
) {}