package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>, QuerydslPredicateExecutor<RoleEntity> {
    Optional<RoleEntity> findByName(RoleEnum name);

    Set<RoleEntity> findAllByNameIn(List<RoleEnum> list);
}
