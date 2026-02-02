package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.RecipeEntity;
import com.querydsl.core.types.Predicate;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, QuerydslPredicateExecutor<RecipeEntity> {

    List<RecipeEntity> findAll(
            @Nonnull
            Predicate predicate,
            @Nonnull
            Sort sort);
}
