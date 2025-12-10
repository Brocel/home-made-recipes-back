package com.example.hmrback.persistence.entity;

import com.example.hmrback.persistence.enums.IngredientType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 50, nullable = false)
    private IngredientType ingredientType;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "NORMALIZED_NAME")
    private String normalizedName;

    // Maybe put some more info later (calories, etc.)
}
