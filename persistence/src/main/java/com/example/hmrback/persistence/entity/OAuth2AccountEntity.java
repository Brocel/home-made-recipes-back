package com.example.hmrback.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "OAUTH2_ACCOUNT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PROVIDER", length = 100, nullable = false, unique = true)
    private String provider;

    @Column(name = "PROVIDER_ID", nullable = false, unique = true)
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OAuth2AccountEntity that)) return false;
        return Objects.equals(provider, that.provider) && Objects.equals(providerId, that.providerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, providerId);
    }
}
