package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>, QuerydslPredicateExecutor<UserEntity> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
