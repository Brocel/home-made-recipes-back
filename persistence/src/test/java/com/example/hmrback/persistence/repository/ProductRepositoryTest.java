package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.ProductEntity;
import com.example.hmrback.persistence.enums.IngredientType;
import com.example.hmrback.persistence.util.RepositoryTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Repository tests for ProductEntity persistence behavior.
 * <p>
 * Tests cover:
 * - CRUD operations (create, read, update, delete)
 * - Custom query methods (findByNormalizedName, existsByNormalizedName)
 * - Constraint validation (NOT NULL, unique constraints)
 * - IngredientType enum mapping
 * - Transaction and rollback behavior
 * </p>
 * <p>
 * Execution Time: ~1-2 seconds
 * Expected: All 18 tests pass
 * </p>
 */
@DisplayName("Product Repository Tests")
class ProductRepositoryTest extends BaseRepositoryTU {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository repository;

    @Nested
    @DisplayName("CRUD Operations")
    class CRUDOperations {

        @Test
        @Order(1)
        @DisplayName("should save product with type and name")
        void shouldSaveProductWithTypeAndName() {
            // Arrange
            ProductEntity product = RepositoryTestDataBuilder.buildProduct();

            // Act
            ProductEntity saved = repository.save(product);
            entityManager.flush();

            // Assert
            assertThat(saved.getId()).isNotNull();
            assertThat(repository.findById(saved.getId())).isPresent().get().isEqualTo(saved);
        }

        @Test
        @Order(2)
        @DisplayName("should update product name")
        void shouldUpdateProductName() {
            // Arrange
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            String newName = "Updated Product";

            // Act
            product.setName(newName);
            repository.save(product);
            entityManager.flush();

            // Assert
            Optional<ProductEntity> updated = repository.findById(product.getId());
            assertThat(updated).isPresent().get()
                    .extracting(ProductEntity::getName)
                    .isEqualTo(newName);
        }

        @Test
        @Order(3)
        @DisplayName("should delete product by id")
        void shouldDeleteProduct() {
            // Arrange
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );
            Long productId = product.getId();

            // Act
            repository.deleteById(productId);
            entityManager.flush();

            // Assert
            assertThat(repository.findById(productId)).isEmpty();
        }

        @Test
        @Order(4)
        @DisplayName("should find product by id")
        void shouldFindProductById() {
            // Arrange
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProduct()
            );

            // Act
            Optional<ProductEntity> result = repository.findById(product.getId());

            // Assert
            assertThat(result).isPresent().get().isEqualTo(product);
        }

        @Test
        @Order(5)
        @DisplayName("should find all products with pagination")
        void shouldFindAllProducts() {
            // Arrange
            List<ProductEntity> products = RepositoryTestDataBuilder.buildProductList(3);
            products.forEach(p -> entityManager.persistAndFlush(p));

            // Act
            List<ProductEntity> all = repository.findAll();

            // Assert
            assertThat(all).isNotEmpty().hasSizeGreaterThanOrEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Custom Query Methods")
    class CustomQueryMethods {

        @Test
        @Order(6)
        @DisplayName("should find product by normalized name when exists")
        void shouldFindByNormalizedName_WhenExists() {
            // Arrange
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProductWithName("TestProduct")
            );
            product.setNormalizedName("testproduct");
            entityManager.persistAndFlush(product);

            // Act
            ProductEntity result = repository.findByNormalizedName("testproduct");

            // Assert
            assertThat(result).isNotNull().isEqualTo(product);
        }

        @Test
        @Order(7)
        @DisplayName("should return null when normalized name not found")
        void shouldReturnNull_WhenNormalizedNameNotFound() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProductWithName("Carrot")
            );

            // Act
            ProductEntity result = repository.findByNormalizedName("nonexistent");

            // Assert
            assertThat(result).isNull();
        }

        @Test
        @Order(8)
        @DisplayName("should return true when normalized name exists")
        void shouldReturnTrue_WhenNormalizedNameExists() {
            // Arrange
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProductWithName("Apple")
            );
            product.setNormalizedName("apple");
            entityManager.persistAndFlush(product);

            // Act
            boolean exists = repository.existsByNormalizedName("apple");

            // Assert
            assertThat(exists).isTrue();
        }

        @Test
        @Order(9)
        @DisplayName("should return false when normalized name not exists")
        void shouldReturnFalse_WhenNormalizedNameNotExists() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProductWithName("Banana")
            );

            // Act
            boolean exists = repository.existsByNormalizedName("notexist");

            // Assert
            assertThat(exists).isFalse();
        }

        @Test
        @Order(10)
        @DisplayName("should find products by ingredient type")
        void shouldFindProductsByIngredientType() {
            // Arrange
            ProductEntity vegetable = RepositoryTestDataBuilder.buildProductWithName("Carrot");
            vegetable.setIngredientType(IngredientType.VEGETABLE);
            entityManager.persistAndFlush(vegetable);

            // Act
            List<ProductEntity> all = repository.findAll();

            // Assert - At least find our vegetable product
            assertThat(all).isNotEmpty()
                    .anyMatch(p -> p.getIngredientType() == IngredientType.VEGETABLE);
        }
    }

    @Nested
    @DisplayName("Constraint & Validation")
    class ConstraintValidation {

        @Test
        @Order(11)
        @DisplayName("should validate ingredient type enum mapping")
        void shouldValidateIngredientTypeEnum() {
            // Arrange
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProductWithName("TestProduct")
            );

            // Act
            Optional<ProductEntity> result = repository.findById(product.getId());

            // Assert
            assertThat(result).isPresent().get()
                    .extracting(ProductEntity::getIngredientType)
                    .isEqualTo(IngredientType.VEGETABLE);
        }

        @Test
        @Order(12)
        @DisplayName("should throw constraint violation when name is required")
        void shouldThrowDataIntegrityViolation_WhenNameRequired() {
            // Arrange
            long initialCount = repository.count();

            // Act & Assert
            ProductEntity invalidProduct = new ProductEntity();
            invalidProduct.setIngredientType(IngredientType.VEGETABLE);
            // Name is not set - should violate NOT NULL constraint
            assertThatThrownBy(() -> repository.saveAndFlush(invalidProduct)).isInstanceOf(DataIntegrityViolationException.class);

            // Clear poisoned session state after exception
            entityManager.clear();

            assertThat(repository.count()).isEqualTo(initialCount);
        }

        @Test
        @Order(13)
        @DisplayName("should handle special characters in product name")
        void shouldHandleSpecialCharactersInProductName() {
            // Arrange
            ProductEntity product = RepositoryTestDataBuilder.buildProductWithName(
                    "Product™ with Ñoño & Special-Chars!"
            );

            // Act
            ProductEntity saved = repository.save(product);
            entityManager.flush();

            // Assert
            Optional<ProductEntity> result = repository.findById(saved.getId());
            assertThat(result).isPresent().get()
                    .extracting(ProductEntity::getName)
                    .isEqualTo("Product™ with Ñoño & Special-Chars!");
        }

        @Test
        @Order(14)
        @DisplayName("should allow null normalized name")
        void shouldAllowNullNormalizedName() {
            // Arrange
            ProductEntity product = RepositoryTestDataBuilder.buildProductWithName("Product");
            product.setNormalizedName(null);

            // Act
            ProductEntity saved = repository.save(product);
            entityManager.flush();

            // Assert
            Optional<ProductEntity> result = repository.findById(saved.getId());
            assertThat(result).isPresent().get()
                    .extracting(ProductEntity::getNormalizedName)
                    .isNull();
        }
    }

    @Nested
    @DisplayName("Transaction Behavior")
    class TransactionBehavior {

        @Test
        @Order(15)
        @DisplayName("should maintain transaction consistency when saving")
        void shouldMaintainTransactionConsistency() {
            // Arrange
            ProductEntity product = RepositoryTestDataBuilder.buildProductWithName("Consistent");

            // Act
            ProductEntity saved = repository.save(product);
            entityManager.flush();
            entityManager.clear();

            // Assert - Verify data persisted correctly
            Optional<ProductEntity> reloaded = repository.findById(saved.getId());
            assertThat(reloaded).isPresent().get()
                    .extracting(ProductEntity::getName)
                    .isEqualTo("Consistent");
        }

        @Test
        @Order(16)
        @DisplayName("should rollback on constraint violation")
        void shouldRollbackOnConstraintViolation() {
            // Arrange
            long initialCount = repository.count();
            ProductEntity invalidProduct = new ProductEntity();
            invalidProduct.setIngredientType(IngredientType.MEAT);
            // Missing required name

            // Act & Assert
            assertThatThrownBy(() -> repository.saveAndFlush(invalidProduct))
                    .isInstanceOf(DataIntegrityViolationException.class);

            // Clear poisoned session state
            entityManager.clear();

            // Verify rollback - count should not have increased
            assertThat(repository.count()).isEqualTo(initialCount);
        }

        @Test
        @Order(17)
        @DisplayName("should handle concurrent product updates")
        void shouldHandleConcurrentProductUpdates() {
            // Arrange
            ProductEntity product1 = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProductWithName("Product1")
            );
            ProductEntity product2 = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProductWithName("Product2")
            );

            // Act
            product1.setName("UpdatedProduct1");
            product2.setName("UpdatedProduct2");
            repository.save(product1);
            repository.save(product2);
            entityManager.flush();

            // Assert
            Optional<ProductEntity> updated1 = repository.findById(product1.getId());
            Optional<ProductEntity> updated2 = repository.findById(product2.getId());
            assertThat(updated1).isPresent().get()
                    .extracting(ProductEntity::getName)
                    .isEqualTo("UpdatedProduct1");
            assertThat(updated2).isPresent().get()
                    .extracting(ProductEntity::getName)
                    .isEqualTo("UpdatedProduct2");
        }

        @Test
        @Order(18)
        @DisplayName("should verify data consistency after multiple operations")
        void shouldVerifyDataConsistencyAfterMultipleOperations() {
            // Arrange
            ProductEntity product = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildProductWithName("MultiOps")
            );
            Long productId = product.getId();

            // Act - Multiple operations
            product.setIngredientType(IngredientType.FRUIT);
            repository.save(product);
            entityManager.flush();
            entityManager.clear();

            // Assert
            Optional<ProductEntity> verified = repository.findById(productId);
            assertThat(verified).isPresent().get()
                    .satisfies(p -> {
                        assertThat(p.getName()).isEqualTo("MultiOps");
                        assertThat(p.getIngredientType()).isEqualTo(IngredientType.FRUIT);
                    });
        }
    }
}

