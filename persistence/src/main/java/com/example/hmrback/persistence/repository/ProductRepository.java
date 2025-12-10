package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, QuerydslPredicateExecutor<ProductEntity> {
    boolean existsByNormalizedName(String name);

    ProductEntity findByNormalizedName(String name);
}
