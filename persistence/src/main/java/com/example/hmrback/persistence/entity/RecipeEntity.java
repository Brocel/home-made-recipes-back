package com.example.hmrback.persistence.entity;

import com.example.hmrback.persistence.enums.RecipeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RECIPE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", length = 2000, nullable = false)
    private String description;

    @Column(name = "PREPARATION_TIME")
    private Integer preparationTime; // in minutes

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 50, nullable = false)
    private RecipeType recipeType;

    @Column(name = "PUBLICATION_DATE")
    private LocalDate publicationDate;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientEntity> ingredientList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepEntity> stepList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity author;
}
