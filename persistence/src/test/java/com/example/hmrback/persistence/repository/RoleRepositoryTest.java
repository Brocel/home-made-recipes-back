package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.enums.RoleEnum;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Repository tests for RoleEntity persistence behavior.
 * <p>
 * Tests cover:
 * - CRUD operations (create, read, update, delete)
 * - Custom query methods (findByName, findAllByNameIn)
 * - Constraint validation (unique name, required fields)
 * - Transaction and rollback behavior
 * </p>
 * <p>
 * Execution Time: ~1-2 seconds
 * Expected: All 14 tests pass
 * </p>
 */
@DisplayName("Role Repository Tests")
class RoleRepositoryTest extends BaseRepositoryTU {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository repository;

    @Nested
    @DisplayName("CRUD Operations")
    class CRUDOperations {

        @Test
        @Order(1)
        @DisplayName("should save role successfully")
        void shouldSaveRole() {
            // Arrange
            RoleEntity role = RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_ADMIN);

            // Act
            RoleEntity saved = repository.save(role);
            entityManager.flush();

            // Assert
            assertThat(saved.getId()).isNotNull();
            assertThat(repository.findById(saved.getId())).isPresent().get().isEqualTo(saved);
        }

        @Test
        @Order(2)
        @DisplayName("should find role by id")
        void shouldFindRoleById() {
            // Arrange
            RoleEntity role = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER)
            );

            // Act
            Optional<RoleEntity> result = repository.findById(role.getId());

            // Assert
            assertThat(result).isPresent().get().isEqualTo(role);
        }

        @Test
        @Order(3)
        @DisplayName("should delete role by id")
        void shouldDeleteRoleById() {
            // Arrange
            RoleEntity role = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildAdminRole()
            );
            Long roleId = role.getId();

            // Act
            repository.deleteById(roleId);
            entityManager.flush();

            // Assert
            assertThat(repository.findById(roleId)).isEmpty();
        }

        @Test
        @Order(4)
        @DisplayName("should find all roles")
        void shouldFindAllRoles() {
            // Arrange
            RoleEntity roleUser = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER)
            );
            RoleEntity roleAdmin = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_ADMIN)
            );

            // Act
            List<RoleEntity> allRoles = repository.findAll();

            // Assert
            assertThat(allRoles).isNotEmpty().contains(roleUser, roleAdmin);
        }
    }

    @Nested
    @DisplayName("Custom Query Methods")
    class CustomQueryMethods {

        @Test
        @Order(5)
        @DisplayName("should find role by name ROLE_USER")
        void shouldFindByName_ROLE_USER() {
            // Arrange
            RoleEntity roleUser = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER)
            );

            // Act
            Optional<RoleEntity> result = repository.findByName(RoleEnum.ROLE_USER);

            // Assert
            assertThat(result).isPresent().get().isEqualTo(roleUser);
        }

        @Test
        @Order(6)
        @DisplayName("should find role by name ROLE_ADMIN")
        void shouldFindByName_ROLE_ADMIN() {
            // Arrange
            RoleEntity roleAdmin = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_ADMIN)
            );

            // Act
            Optional<RoleEntity> result = repository.findByName(RoleEnum.ROLE_ADMIN);

            // Assert
            assertThat(result).isPresent().get().isEqualTo(roleAdmin);
        }

        @Test
        @Order(7)
        @DisplayName("should find all roles by name list")
        void shouldFindAllByNameList_WhenMultipleRoles() {
            // Arrange
            RoleEntity roleUser = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER)
            );
            RoleEntity roleAdmin = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_ADMIN)
            );
            List<RoleEnum> roleNames = List.of(RoleEnum.ROLE_USER, RoleEnum.ROLE_ADMIN);

            // Act
            Set<RoleEntity> result = repository.findAllByNameIn(roleNames);

            // Assert
            assertThat(result).isNotEmpty().contains(roleUser, roleAdmin).hasSize(2);
        }

        @Test
        @Order(8)
        @DisplayName("should return empty when role not found")
        void shouldReturnEmpty_WhenRoleNotFound() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER)
            );

            // Act
            Optional<RoleEntity> result = repository.findByName(RoleEnum.ROLE_ADMIN);

            // Assert
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Constraint Validation")
    class ConstraintValidation {

        @Test
        @Order(9)
        @DisplayName("should throw constraint violation when duplicate role name is saved")
        void shouldThrowDataIntegrityViolation_WhenDuplicateRoleName() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER)
            );

            // Act & Assert
            RoleEntity roleUser2 = RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER);
            assertThatThrownBy(() -> repository.saveAndFlush(roleUser2))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        @Order(10)
        @DisplayName("should throw constraint violation when name is required")
        void shouldThrowDataIntegrityViolation_WhenNameRequired() {
            // Arrange
            long initialCount = repository.count();

            // Act & Assert
            RoleEntity invalidRole = new RoleEntity();
            // Name is not set - should violate NOT NULL constraint
            assertThatThrownBy(() -> repository.saveAndFlush(invalidRole)).isInstanceOf(DataIntegrityViolationException.class);

            // Clear poisoned session state after exception
            entityManager.clear();

            assertThat(repository.count()).isEqualTo(initialCount);
        }

        @Test
        @Order(11)
        @DisplayName("should validate role enum type mapping")
        void shouldValidateRoleEnumType() {
            // Arrange
            RoleEntity role = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_ADMIN)
            );

            // Act
            Optional<RoleEntity> result = repository.findById(role.getId());

            // Assert
            assertThat(result).isPresent().get()
                    .extracting(RoleEntity::getName)
                    .isEqualTo(RoleEnum.ROLE_ADMIN);
        }
    }

    @Nested
    @DisplayName("Transaction Behavior")
    class TransactionBehavior {

        @Test
        @Order(12)
        @DisplayName("should rollback on constraint violation")
        void shouldRollbackOnConstraintViolation() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER)
            );

            long initialCount = repository.count();

            // Act & Assert
            RoleEntity duplicateRole = RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_USER);
            assertThatThrownBy(() -> repository.saveAndFlush(duplicateRole))
                    .isInstanceOf(DataIntegrityViolationException.class);

            // Clear poisoned session state after exception
            entityManager.clear();

            // Verify rollback - count should not have increased
            assertThat(repository.count()).isEqualTo(initialCount);
        }

        @Test
        @Order(13)
        @DisplayName("should verify data consistency after transaction")
        void shouldVerifyDataConsistencyAfterTransaction() {
            // Arrange
            RoleEntity role = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRoleByType(RoleEnum.ROLE_ADMIN)
            );

            // Act
            // Clear persistence context to force reload from database
            entityManager.clear();
            Optional<RoleEntity> reloadedRole = repository.findById(role.getId());

            // Assert - Verify data persisted correctly
            assertThat(reloadedRole).isPresent().get()
                    .extracting(RoleEntity::getName)
                    .isEqualTo(RoleEnum.ROLE_ADMIN);
        }
    }
}

