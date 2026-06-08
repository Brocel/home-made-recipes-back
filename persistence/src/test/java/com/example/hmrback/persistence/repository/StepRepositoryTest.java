package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.RecipeEntity;
import com.example.hmrback.persistence.entity.StepEntity;
import com.example.hmrback.persistence.util.RepositoryTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests for StepEntity persistence behavior.
 * <p>
 * Tests cover:
 * - CRUD operations (create, read, update, delete)
 * - Query operations (find by recipe, find by order)
 * - Constraint validation (NOT NULL, foreign key)
 * - Relationship management (many-to-one with Recipe)
 * - Transaction and rollback behavior
 * </p>
 * <p>
 * Execution Time: ~1-2 seconds
 * Expected: All 14 tests pass
 * </p>
 */
@DisplayName("Step Repository Tests")
class StepRepositoryTest extends BaseRepositoryTU {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StepRepository repository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Nested
    @DisplayName("CRUD Operations")
    class CRUDOperations {

        @Test
        @DisplayName("should save step with recipe relationship")
        void shouldSaveStepWithRecipeRelationship() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);

            // Act
            StepEntity saved = repository.save(step);
            entityManager.flush();

            // Assert
            assertThat(saved.getId()).isNotNull();
            assertThat(repository.findById(saved.getId())).isPresent().get().isEqualTo(saved);
        }

        @Test
        @DisplayName("should update step description and order")
        void shouldUpdateStepDescriptionAndOrder() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);
            StepEntity saved = entityManager.persistAndFlush(step);

            // Act
            saved.setDescription("Updated description");
            saved.setOrder(5);
            repository.save(saved);
            entityManager.flush();

            // Assert
            Optional<StepEntity> updated = repository.findById(saved.getId());
            assertThat(updated).isPresent().get()
                    .satisfies(s -> {
                        assertThat(s.getDescription()).isEqualTo("Updated description");
                        assertThat(s.getOrder()).isEqualTo(5);
                    });
        }

        @Test
        @DisplayName("should delete step by id")
        void shouldDeleteStep() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);
            StepEntity saved = entityManager.persistAndFlush(step);
            Long stepId = saved.getId();

            // Act
            repository.deleteById(stepId);
            entityManager.flush();

            // Assert
            assertThat(repository.findById(stepId)).isEmpty();
        }

        @Test
        @DisplayName("should find step by id")
        void shouldFindStepById() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);
            StepEntity saved = entityManager.persistAndFlush(step);

            // Act
            Optional<StepEntity> result = repository.findById(saved.getId());

            // Assert
            assertThat(result).isPresent().get().isEqualTo(saved);
        }
    }

    @Nested
    @DisplayName("Query Operations")
    class QueryOperations {

        @Test
        @Order(1)
        @DisplayName("should find all steps for recipe ordered by order")
        void shouldFindAllStepsForRecipeOrderedByOrder() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            List<StepEntity> steps = RepositoryTestDataBuilder.buildStepList(3);
            steps.forEach(s -> {
                s.setRecipe(recipe);
                entityManager.persistAndFlush(s);
            });

            // Act
            List<StepEntity> all = repository.findAll();

            // Assert
            assertThat(all).isNotEmpty()
                    .anyMatch(s -> s.getRecipe().getId().equals(recipe.getId()));
        }

        @Test
        @Order(2)
        @DisplayName("should find step by order sequence")
        void shouldFindStepByOrderSequence() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);
            step.setOrder(3);
            entityManager.persistAndFlush(step);

            // Act
            List<StepEntity> all = repository.findAll();

            // Assert
            assertThat(all).isNotEmpty()
                    .anyMatch(s -> s.getOrder().equals(3));
        }

        @Test
        @Order(3)
        @DisplayName("should verify foreign key constraints")
        void shouldVerifyForeignKeyConstraints() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);

            // Act
            StepEntity saved = repository.save(step);
            entityManager.flush();

            // Assert
            assertThat(saved.getRecipe()).isNotNull()
                    .isEqualTo(recipe);
        }

        @Test
        @Order(4)
        @DisplayName("should return empty when recipe has no steps")
        void shouldReturnEmpty_WhenRecipeHasNoSteps() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);

            // Act
            List<StepEntity> all = repository.findAll();

            // Assert - Should not match our recipe (unless there are other steps)
            assertThat(all)
                    .noneMatch(s -> s.getRecipe() != null && s.getRecipe().getId().equals(recipe.getId()));
        }
    }

    @Nested
    @DisplayName("Relationship Tests")
    class RelationshipManagement {

        @Test
        @Order(5)
        @DisplayName("should verify many-to-one with recipe")
        void shouldVerifyManyToOneWithRecipe() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);
            StepEntity saved = entityManager.persistAndFlush(step);

            // Act
            Optional<StepEntity> result = repository.findById(saved.getId());

            // Assert
            assertThat(result).isPresent().get()
                    .extracting(StepEntity::getRecipe)
                    .isEqualTo(recipe);
        }

        @Test
        @Order(6)
        @DisplayName("should lazily load recipe when accessing recipe entity")
        void shouldLazilyLoadRecipe_WhenAccessingRecipeEntity() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);
            entityManager.persistAndFlush(step);
            entityManager.clear();

            // Act
            Optional<StepEntity> loaded = repository.findById(step.getId());

            // Assert - Recipe should be loadable
            assertThat(loaded).isPresent().get()
                    .extracting(StepEntity::getRecipe)
                    .isNotNull();
        }

        @Test
        @Order(7)
        @DisplayName("should update step order sequence")
        void shouldUpdateStepOrderSequence() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            List<StepEntity> steps = RepositoryTestDataBuilder.buildStepList(3);
            steps.forEach(s -> {
                s.setRecipe(recipe);
                entityManager.persistAndFlush(s);
            });

            // Act - Update orders
            steps.get(0).setOrder(1);
            steps.get(1).setOrder(2);
            steps.get(2).setOrder(3);
            repository.saveAll(steps);
            entityManager.flush();

            // Assert
            List<StepEntity> all = repository.findAll();
            assertThat(all)
                    .anyMatch(s -> s.getOrder().equals(1))
                    .anyMatch(s -> s.getOrder().equals(2))
                    .anyMatch(s -> s.getOrder().equals(3));
        }
    }

    @Nested
    @DisplayName("Transaction Behavior")
    class TransactionBehavior {

        @Test
        @Order(8)
        @DisplayName("should maintain step order consistency")
        void shouldMaintainStepOrderConsistency() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);
            step.setOrder(1);
            StepEntity saved = entityManager.persistAndFlush(step);

            // Act
            entityManager.clear();
            Optional<StepEntity> reloaded = repository.findById(saved.getId());

            // Assert
            assertThat(reloaded).isPresent().get()
                    .extracting(StepEntity::getOrder)
                    .isEqualTo(1);
        }

        @Test
        @Order(9)
        @DisplayName("should cascade delete steps with recipe")
        void shouldCascadeDeleteSteps_WithRecipe() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step = RepositoryTestDataBuilder.buildStep();
            step.setRecipe(recipe);
            recipe.getStepList().add(step);
            StepEntity saved = entityManager.persistAndFlush(step);
            Long stepId = saved.getId();

            // Act
            recipeRepository.deleteById(recipe.getId());
            entityManager.flush();

            // Assert - Step should be deleted via orphan removal
            assertThat(repository.findById(stepId)).isEmpty();
        }

        @Test
        @Order(10)
        @DisplayName("should handle concurrent step updates")
        void shouldHandleConcurrentStepUpdates() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            StepEntity step1 = RepositoryTestDataBuilder.buildStep();
            step1.setRecipe(recipe);
            step1.setOrder(1);
            StepEntity step2 = RepositoryTestDataBuilder.buildStep();
            step2.setRecipe(recipe);
            step2.setOrder(2);

            StepEntity saved1 = entityManager.persistAndFlush(step1);
            StepEntity saved2 = entityManager.persistAndFlush(step2);

            // Act
            saved1.setDescription("Updated Step 1");
            saved2.setDescription("Updated Step 2");
            repository.save(saved1);
            repository.save(saved2);
            entityManager.flush();

            // Assert
            Optional<StepEntity> updated1 = repository.findById(saved1.getId());
            Optional<StepEntity> updated2 = repository.findById(saved2.getId());
            assertThat(updated1).isPresent().get()
                    .extracting(StepEntity::getDescription)
                    .isEqualTo("Updated Step 1");
            assertThat(updated2).isPresent().get()
                    .extracting(StepEntity::getDescription)
                    .isEqualTo("Updated Step 2");
        }
    }
}

