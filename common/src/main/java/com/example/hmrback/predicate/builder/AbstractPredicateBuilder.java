package com.example.hmrback.predicate.builder;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPredicateBuilder {

    public final List<BooleanExpression> expressions = new ArrayList<>();

    public Predicate build() {
        return expressions.stream()
            .reduce(BooleanExpression::and)
            .orElse(null);
    }
}
