package com.example.hmrback.predicate.builder;

import com.example.hmrback.persistence.entity.QRecipeEntity;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.enums.RecipeType;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipePredicateBuilder extends AbstractPredicateBuilder {

    private static final QRecipeEntity recipe = QRecipeEntity.recipeEntity;

    public RecipePredicateBuilder titleContains(String title) {
        if (!StringUtils.isNullOrEmpty(title)) {
            expressions.add(recipe.title.containsIgnoreCase(title));
        }
        return this;
    }

    public RecipePredicateBuilder descriptionContains(String description) {
        if (!StringUtils.isNullOrEmpty(description)) {
            expressions.add(recipe.description.containsIgnoreCase(description));
        }
        return this;
    }

    public RecipePredicateBuilder maxPrepTime(Integer maxPrepTime) {
        if (maxPrepTime != null) {
            expressions.add(recipe.preparationTime.loe(maxPrepTime));
        }
        return this;
    }

    public RecipePredicateBuilder recipeTypeIn(List<RecipeType> typeList) {
        if (!CollectionUtils.isEmpty(typeList)) {
            expressions.add(recipe.recipeType.in(typeList));
        }
        return this;
    }

    public RecipePredicateBuilder authorUsernameContains(String authorUsername) {
        if (!StringUtils.isNullOrEmpty(authorUsername)) {
            expressions.add(recipe.author.username.containsIgnoreCase(authorUsername));
        }
        return this;
    }

    public RecipePredicateBuilder ingredientNameIn(List<String> ingredientNameList) {
        if (!CollectionUtils.isEmpty(ingredientNameList)) {
            expressions.add(recipe.ingredientList.any().product.name.in(ingredientNameList));
        }
        return this;
    }

    public RecipePredicateBuilder ingrendientTypeIn(List<IngredientType> ingredientTypeList) {
        if (!CollectionUtils.isEmpty(ingredientTypeList)) {
            expressions.add(recipe.ingredientList.any().product.ingredientType.in(ingredientTypeList));
        }
        return this;
    }
}
