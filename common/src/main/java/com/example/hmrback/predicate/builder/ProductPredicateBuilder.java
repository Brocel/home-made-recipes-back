package com.example.hmrback.predicate.builder;

import com.example.hmrback.persistence.entity.QProductEntity;
import com.example.hmrback.utils.NormalizeUtils;
import com.querydsl.core.util.StringUtils;

public class ProductPredicateBuilder extends AbstractPredicateBuilder {

    private static final QProductEntity product = QProductEntity.productEntity;

    public ProductPredicateBuilder normalizedNameContains(String name) {
        if (!StringUtils.isNullOrEmpty(name)) {
            expressions.add(product.normalizedName.containsIgnoreCase(NormalizeUtils.normalizeText(name)));
        }
        return this;
    }

    public ProductPredicateBuilder normalizedNameEqualsIgnoreCase(String name) {
        if (!StringUtils.isNullOrEmpty(name)) {
            expressions.add(product.normalizedName.equalsIgnoreCase(NormalizeUtils.normalizeText(name)));
        }
        return this;
    }

}
