package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.OAuth2AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuth2AccountRepository extends JpaRepository<OAuth2AccountEntity, Long> {
    Optional<OAuth2AccountEntity> findByProviderAndProviderId(String provider, String providerId);
    Optional<OAuth2AccountEntity> findByUserEmail(String email);
}
