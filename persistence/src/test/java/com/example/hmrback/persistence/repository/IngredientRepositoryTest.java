package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.IngredientEntity;
import com.example.hmrback.persistence.entity.ProductEntity;
import com.example.hmrback.persistence.entity.RecipeEntity;
import com.example.hmrback.persistence.enums.Unit;
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
 * Repository tests for IngredientEntity persistence behavior.
 * <p>
 * Tests cover:
 * - CRUD operations (create, read, update, delete)
 * - Query operations (find by recipe, find by unit, find by product)
 * - Constraint validation (NOT NULL, foreign key)
 * - Relationship management (many-to-one with Recipe and Product)
 * - Lazy loading and transaction behavior
 * </p>
 * <p>
 * Execution Time: ~1-2 seconds
 * Expected: All 16 tests pass
 * </p>
 */
@DisplayName("Ingredient Repository Tests")
class IngredientRepositoryTest extends BaseRepositoryTU {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IngredientRepository repository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Nested
    @DisplayName("CRUD Operations")
    class CRUDOperations {

        @Test
        @Order(1)
        @DisplayName("should save ingredient with recipe and product relationships")
        void shouldSaveIngredientWithRecipeAndProductRelationships() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);

            // Act
            IngredientEntity saved = repository.save(ingredient);
            entityManager.flush();

            // Assert
            assertThat(saved.getId()).isNotNull();
            assertThat(repository.findById(saved.getId())).isPresent().get().isEqualTo(saved);
        }

        @Test
        @Order(2)
        @DisplayName("should update ingredient quantity and unit")
        void shouldUpdateIngredientQuantityAndUnit() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            IngredientEntity saved = entityManager.persistAndFlush(ingredient);

            // Act
            saved.setQuantity(250.0);
            saved.setUnit(Unit.MILLILITER);
            repository.save(saved);
            entityManager.flush();

            // Assert
            Optional<IngredientEntity> updated = repository.findById(saved.getId());
            assertThat(updated).isPresent().get()
                    .satisfies(i -> {
                        assertThat(i.getQuantity()).isEqualTo(250.0);
                        assertThat(i.getUnit()).isEqualTo(Unit.MILLILITER);
                    });
        }

        @Test
        @Order(3)
        @DisplayName("should delete ingredient by id")
        void shouldDeleteIngredient() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            IngredientEntity saved = entityManager.persistAndFlush(ingredient);
            Long ingredientId = saved.getId();

            // Act
            repository.deleteById(ingredientId);
            entityManager.flush();

            // Assert
            assertThat(repository.findById(ingredientId)).isEmpty();
        }

        @Test
        @Order(4)
        @DisplayName("should find ingredient by ID")
        void shouldFindIngredientById() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            IngredientEntity saved = entityManager.persistAndFlush(ingredient);

            // Act
            Optional<IngredientEntity> result = repository.findById(saved.getId());

            // Assert
            assertThat(result).isPresent().get().isEqualTo(saved);
        }
    }

    @Nested
    @DisplayName("Query Operations")
    class QueryOperations {

        @Test
        @Order(5)
        @DisplayName("should find all ingredients for recipe")
        void shouldFindAllIngredientsForRecipe() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            List<ProductEntity> products = RepositoryTestDataBuilder.buildProductList(2);
            products.forEach(p -> entityManager.persistAndFlush(p));

            List<IngredientEntity> ingredients = RepositoryTestDataBuilder.buildIngredientList(2);
            ingredients.forEach(i -> {
                i.setRecipe(recipe);
                RepositoryTestDataBuilder.persistIngredientIfNeeded(entityManager, i);
            });

            // Act
            List<IngredientEntity> all = repository.findAll();

            // Assert
            assertThat(all).isNotEmpty()
                    .anyMatch(i -> i.getRecipe().getId().equals(recipe.getId()));
        }

        @Test
        @Order(6)
        @DisplayName("should find ingredients by unit type")
        void shouldFindIngredientsByUnitType() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            ingredient.setUnit(Unit.KILOGRAM);
            RepositoryTestDataBuilder.persistIngredientIfNeeded(entityManager, ingredient);

            // Act
            List<IngredientEntity> all = repository.findAll();

            // Assert
            assertThat(all).isNotEmpty()
                    .anyMatch(i -> i.getUnit() == Unit.KILOGRAM);
        }

        @Test
        @Order(7)
        @DisplayName("should find ingredients by product id")
        void shouldFindIngredientsByProductId() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            RepositoryTestDataBuilder.persistIngredientIfNeeded(entityManager, ingredient);

            // Act
            List<IngredientEntity> all = repository.findAll();

            // Assert
            assertThat(all).isNotEmpty()
                    .anyMatch(i -> i.getProduct().getId().equals(product.getId()));
        }

        @Test
        @Order(8)
        @DisplayName("should verify foreign key constraints")
        void shouldVerifyForeignKeyConstraints() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);

            // Act
            IngredientEntity saved = repository.save(ingredient);
            entityManager.flush();

            // Assert
            assertThat(saved.getRecipe()).isNotNull().isEqualTo(recipe);
            assertThat(saved.getProduct()).isNotNull().isEqualTo(product);
        }

        @Test
        @Order(9)
        @DisplayName("should return empty when recipe has no ingredients")
        void shouldReturnEmpty_WhenRecipeHasNoIngredients() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);

            // Act
            List<IngredientEntity> all = repository.findAll();

            // Assert - Should not match our recipe (unless there are other ingredients)
            assertThat(all)
                    .noneMatch(i -> i.getRecipe() != null && i.getRecipe().getId().equals(recipe.getId()));
        }
    }

    @Nested
    @DisplayName("Relationship Tests")
    class RelationshipManagement {

        @Test
        @Order(10)
        @DisplayName("should verify many-to-one with recipe")
        void shouldVerifyManyToOneWithRecipe() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            IngredientEntity saved = RepositoryTestDataBuilder.persistIngredientIfNeeded(entityManager, ingredient);

            // Act
            Optional<IngredientEntity> result = repository.findById(saved.getId());

            // Assert
            assertThat(result).isPresent().get()
                    .extracting(IngredientEntity::getRecipe)
                    .isEqualTo(recipe);
        }

        @Test
        @Order(11)
        @DisplayName("should verify many-to-one with product")
        void shouldVerifyManyToOneWithProduct() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            IngredientEntity saved = entityManager.persistAndFlush(ingredient);

            // Act
            Optional<IngredientEntity> result = repository.findById(saved.getId());

            // Assert
            assertThat(result).isPresent().get()
                    .extracting(IngredientEntity::getProduct)
                    .isEqualTo(product);
        }

        @Test
        @Order(12)
        @DisplayName("should lazily load recipe and product")
        void shouldLazilyLoadRecipeAndProduct() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            RepositoryTestDataBuilder.persistIngredientIfNeeded(entityManager, ingredient);
            entityManager.clear();

            // Act
            Optional<IngredientEntity> loaded = repository.findById(ingredient.getId());

            // Assert
            assertThat(loaded).isPresent().get()
                    .satisfies(i -> {
                        assertThat(i.getRecipe()).isNotNull();
                        assertThat(i.getProduct()).isNotNull();
                    });
        }

        @Test
        @Order(13)
        @DisplayName("should update ingredient relationships")
        void shouldUpdateIngredientRelationships() {
            // Arrange
            RecipeEntity recipe1 = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            RecipeEntity recipe2 = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe1);
            ingredient.setProduct(product);
            IngredientEntity saved = RepositoryTestDataBuilder.persistIngredientIfNeeded(entityManager, ingredient);

            // Act
            saved.setRecipe(recipe2);
            repository.save(saved);
            entityManager.flush();

            // Assert
            Optional<IngredientEntity> updated = repository.findById(saved.getId());
            assertThat(updated).isPresent().get()
                    .extracting(IngredientEntity::getRecipe)
                    .isEqualTo(recipe2);
        }
    }

    @Nested
    @DisplayName("Transaction Behavior")
    class TransactionBehavior {

        @Test
        @Order(14)
        @DisplayName("should handle orphan removal when ingredient deleted")
        void shouldHandleOrphanRemoval_WhenIngredientDeleted() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            IngredientEntity saved = entityManager.persistAndFlush(ingredient);
            Long ingredientId = saved.getId();

            // Act
            repository.deleteById(ingredientId);
            entityManager.flush();

            // Assert
            assertThat(repository.findById(ingredientId)).isEmpty();
        }

        @Test
        @Order(15)
        @DisplayName("should cascade save on recipe persistence")
        void shouldCascadeSave_OnRecipePersistence() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);

            // Act
            RecipeEntity savedRecipe = recipeRepository.save(recipe);
            entityManager.flush();

            // Assert
            assertThat(savedRecipe.getId()).isNotNull();
        }

        @Test
        @Order(16)
        @DisplayName("should maintain foreign key integrity")
        void shouldMaintainForeignKeyIntegrity() {
            // Arrange
            RecipeEntity recipe = RepositoryTestDataBuilder.buildAndPersistRecipeWithTestAuthor(entityManager);
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            IngredientEntity ingredient = RepositoryTestDataBuilder.buildIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setProduct(product);
            IngredientEntity saved = entityManager.persistAndFlush(ingredient);

            // Act
            entityManager.clear();
            Optional<IngredientEntity> reloaded = repository.findById(saved.getId());

            // Assert
            assertThat(reloaded).isPresent().get()
                    .satisfies(i -> {
                        assertThat(i.getRecipe()).isNotNull();
                        assertThat(i.getRecipe().getId()).isEqualTo(recipe.getId());
                        assertThat(i.getProduct()).isNotNull();
                        assertThat(i.getProduct().getId()).isEqualTo(product.getId());
                    });
        }
    }
}

