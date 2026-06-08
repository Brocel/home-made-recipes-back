package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<StepEntity, Long>, QuerydslPredicateExecutor<StepEntity> {
}
