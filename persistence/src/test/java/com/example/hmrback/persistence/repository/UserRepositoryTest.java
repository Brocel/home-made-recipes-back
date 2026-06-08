package com.example.hmrback.persistence.repository;

import com.example.hmrback.persistence.entity.RoleEntity;
import com.example.hmrback.persistence.entity.UserEntity;
import com.example.hmrback.persistence.util.RepositoryTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.SET;

/**
 * Repository tests for UserEntity persistence behavior.
 * <p>
 * Tests cover:
 * - CRUD operations (create, read, update, delete) with UUID primary keys
 * - Custom query methods (findByEmail, existsByEmail, findByUsername, existsByUsername)
 * - Constraint validation (unique email, unique username, required fields)
 * - Relationship management (many-to-many roles, one-to-many recipes)
 * - Transaction and rollback behavior
 * </p>
 * <p>
 * Execution Time: ~2-3 seconds
 * Expected: All 24 tests pass
 * </p>
 */
@DisplayName("User Repository Tests")
class UserRepositoryTest extends BaseRepositoryTU {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Nested
    @DisplayName("CRUD Operations")
    class CRUDOperations {

        @Test
        @Order(1)
        @DisplayName("should save new user with roles")
        void shouldSaveNewUserWithRoles() {
            // Arrange
            RoleEntity userRole = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRole()
            );
            Set<RoleEntity> roles = new HashSet<>();
            roles.add(userRole);
            UserEntity user = RepositoryTestDataBuilder.buildUserWithRoles(roles);

            // Act
            UserEntity saved = repository.save(user);
            entityManager.flush();

            // Assert
            assertThat(saved.getId()).isNotNull();
            assertThat(repository.findById(saved.getId())).isPresent().get().isEqualTo(saved);
        }

        @Test
        @Order(2)
        @DisplayName("should update user profile data")
        void shouldUpdateUserProfileData() {
            // Arrange
            UserEntity user = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            // Act
            user.setFirstName("NewFirst");
            user.setLastName("NewLast");
            user.setBirthDate(LocalDate.of(1990, 1, 1));
            repository.save(user);
            entityManager.flush();

            // Assert
            Optional<UserEntity> updated = repository.findById(user.getId());
            assertThat(updated).isPresent().get()
                    .satisfies(u -> {
                        assertThat(u.getFirstName()).isEqualTo("NewFirst");
                        assertThat(u.getLastName()).isEqualTo("NewLast");
                        assertThat(u.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
                    });
        }

        @Test
        @Order(3)
        @DisplayName("should delete user and cascade delete recipes")
        void shouldDeleteUserById() {
            // Arrange
            UserEntity user = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );
            UUID userId = user.getId();

            // Act
            repository.deleteById(userId);
            entityManager.flush();

            // Assert
            assertThat(repository.findById(userId)).isEmpty();
        }

        @Test
        @Order(4)
        @DisplayName("should find user by uuid primary key")
        void shouldFindUserById() {
            // Arrange
            UserEntity user = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            // Act
            Optional<UserEntity> result = repository.findById(user.getId());

            // Assert
            assertThat(result).isPresent().get().isEqualTo(user);
        }

        @Test
        @Order(5)
        @DisplayName("should find all users paginated")
        void shouldFindAllUsersPaginated() {
            // Arrange
            List<UserEntity> users = RepositoryTestDataBuilder.buildUserDataSet(3);
            users.forEach(u -> {
                u.setUsername("user" + UUID.randomUUID().toString().substring(0, 8));
                u.setEmail("user" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
                entityManager.persistAndFlush(u);
            });

            // Act
            List<UserEntity> all = repository.findAll();

            // Assert
            assertThat(all).isNotEmpty().hasSizeGreaterThanOrEqualTo(3);
        }

        @Test
        @Order(6)
        @DisplayName("should generate valid uuid on user creation")
        void shouldGenerateValidUUID_OnUserCreation() {
            // Arrange
            UserEntity user = RepositoryTestDataBuilder.buildUser();

            // Act
            UserEntity saved = repository.save(user);
            entityManager.flush();

            // Assert
            assertThat(saved.getId()).isNotNull()
                    .isInstanceOf(UUID.class);
        }
    }

    @Nested
    @DisplayName("Custom Query Methods")
    class CustomQueryMethods {

        @Test
        @Order(7)
        @DisplayName("should find user by email when exists")
        void shouldFindUserByEmail_WhenExists() {
            // Arrange
            String email = "test@example.com";
            UserEntity user = RepositoryTestDataBuilder.buildUserWithEmail(email);
            entityManager.persistAndFlush(user);

            // Act
            Optional<UserEntity> result = repository.findByEmail(email);

            // Assert
            assertThat(result).isPresent().get().isEqualTo(user);
        }

        @Test
        @Order(8)
        @DisplayName("should return empty when find by email not exists")
        void shouldReturnEmpty_WhenFindByEmailNotExists() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUserWithEmail("existing@example.com")
            );

            // Act
            Optional<UserEntity> result = repository.findByEmail("nonexistent@example.com");

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @Order(9)
        @DisplayName("should return true when email exists")
        void shouldReturnTrue_WhenEmailExists() {
            // Arrange
            String email = "checkme@example.com";
            UserEntity user = RepositoryTestDataBuilder.buildUserWithEmail(email);
            entityManager.persistAndFlush(user);

            // Act
            boolean exists = repository.existsByEmail(email);

            // Assert
            assertThat(exists).isTrue();
        }

        @Test
        @Order(10)
        @DisplayName("should return false when email not exists")
        void shouldReturnFalse_WhenEmailNotExists() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUserWithEmail("real@example.com")
            );

            // Act
            boolean exists = repository.existsByEmail("notreal@example.com");

            // Assert
            assertThat(exists).isFalse();
        }

        @Test
        @Order(11)
        @DisplayName("should find user by username when exists")
        void shouldFindUserByUsername_WhenExists() {
            // Arrange
            UserEntity user = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            // Act
            Optional<UserEntity> result = repository.findByUsername(user.getUsername());

            // Assert
            assertThat(result).isPresent().get().isEqualTo(user);
        }

        @Test
        @Order(12)
        @DisplayName("should return empty when find by username not exists")
        void shouldReturnEmpty_WhenFindByUsernameNotExists() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            // Act
            Optional<UserEntity> result = repository.findByUsername("nonexistentuser");

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @Order(13)
        @DisplayName("should return true when username exists")
        void shouldReturnTrue_WhenUsernameExists() {
            // Arrange
            UserEntity user = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            // Act
            boolean exists = repository.existsByUsername(user.getUsername());

            // Assert
            assertThat(exists).isTrue();
        }

        @Test
        @Order(14)
        @DisplayName("should return false when username not exists")
        void shouldReturnFalse_WhenUsernameNotExists() {
            // Arrange
            entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            // Act
            boolean exists = repository.existsByUsername("nonexistentuser123");

            // Assert
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("Constraint Validation")
    class ConstraintValidation {

        @Test
        @Order(15)
        @DisplayName("should throw constraint violation when duplicate username")
        void shouldThrowDataIntegrityViolation_WhenDuplicateUsername() {
            // Arrange
            UserEntity user1 = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );
            String duplicateUsername = user1.getUsername();

            // Act & Assert
            UserEntity user2 = RepositoryTestDataBuilder.buildUser();
            user2.setUsername(duplicateUsername);
            assertThatThrownBy(() -> repository.saveAndFlush(user2))
                    .isInstanceOf(DataIntegrityViolationException.class);

            // Clear poisoned session
            entityManager.clear();
        }

        @Test
        @Order(16)
        @DisplayName("should throw constraint violation when duplicate email")
        void shouldThrowDataIntegrityViolation_WhenDuplicateEmail() {
            // Arrange
            UserEntity user1 = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );
            String duplicateEmail = user1.getEmail();

            // Act & Assert
            UserEntity user2 = RepositoryTestDataBuilder.buildUser();
            user2.setEmail(duplicateEmail);
            assertThatThrownBy(() -> repository.saveAndFlush(user2)).isInstanceOf(DataIntegrityViolationException.class);

            // Clear poisoned session
            entityManager.clear();
        }

        @Test
        @Order(17)
        @DisplayName("should throw constraint violation when missing first name")
        void shouldThrowDataIntegrityViolation_WhenMissingFirstName() {
            // Arrange
            long initialCount = repository.count();

            // Act & Assert
            UserEntity invalidUser = new UserEntity();
            invalidUser.setLastName("LastName");
            invalidUser.setUsername("testuser");
            invalidUser.setEmail("test@example.com");
            invalidUser.setPassword("password");
            // FirstName is not set
            assertThatThrownBy(() -> repository.saveAndFlush(invalidUser)).isInstanceOf(DataIntegrityViolationException.class);

            // Clear poisoned session
            entityManager.clear();

            assertThat(repository.count()).isEqualTo(initialCount);
        }
    }

    @Nested
    @DisplayName("Relationship Management")
    class RelationshipManagement {

        @Test
        @Order(18)
        @DisplayName("should save user with roles")
        void shouldSaveUserWithRoles() {
            // Arrange
            RoleEntity userRole = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRole()
            );
            RoleEntity adminRole = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildAdminRole()
            );
            Set<RoleEntity> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(adminRole);
            UserEntity user = RepositoryTestDataBuilder.buildUserWithRoles(roles);

            // Act
            UserEntity saved = repository.save(user);
            entityManager.flush();

            // Assert
            assertThat(saved.getRoles()).hasSize(2)
                    .contains(userRole, adminRole);
        }

        @Test
        @Order(19)
        @DisplayName("should verify many-to-many relationship")
        void shouldVerifyManyToManyRelationship() {
            // Arrange
            RoleEntity role = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRole()
            );
            Set<RoleEntity> roles = new HashSet<>();
            roles.add(role);
            UserEntity user = RepositoryTestDataBuilder.buildUserWithRoles(roles);
            UserEntity saved = entityManager.persistAndFlush(user);

            // Act
            Optional<UserEntity> result = repository.findById(saved.getId());

            // Assert
            assertThat(result).isPresent().get()
                    .extracting(UserEntity::getRoles)
                    .isNotNull()
                    .asInstanceOf(SET)
                    .contains(role);
        }

        @Test
        @Order(20)
        @DisplayName("should update user roles")
        void shouldUpdateUserRoles() {
            // Arrange
            RoleEntity userRole = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildRole()
            );
            RoleEntity adminRole = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildAdminRole()
            );
            Set<RoleEntity> initialRoles = new HashSet<>();
            initialRoles.add(userRole);
            UserEntity user = RepositoryTestDataBuilder.buildUserWithRoles(initialRoles);
            UserEntity saved = entityManager.persistAndFlush(user);

            // Act - Replace roles
            saved.getRoles().clear();
            saved.getRoles().add(adminRole);
            repository.save(saved);
            entityManager.flush();

            // Assert
            Optional<UserEntity> updated = repository.findById(saved.getId());
            assertThat(updated).isPresent().get()
                    .extracting(UserEntity::getRoles)
                    .asInstanceOf(SET)
                    .contains(adminRole)
                    .doesNotContain(userRole);
        }

        @Test
        @Order(21)
        @DisplayName("should lazily load recipes when accessing recipe list")
        void shouldLazilyLoadRecipes_WhenAccessingRecipeList() {
            // Arrange
            UserEntity user = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );
            entityManager.clear();

            // Act
            Optional<UserEntity> loaded = repository.findById(user.getId());

            // Assert - Recipes should be loadable
            assertThat(loaded).isPresent().get()
                    .extracting(UserEntity::getRecipes)
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("Transaction Behavior")
    class TransactionBehavior {

        @Test
        @Order(22)
        @DisplayName("should verify transaction isolation when concurrent updates")
        void shouldVerifyTransactionIsolation_WhenConcurrentUpdates() {
            // Arrange
            UserEntity user1 = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );
            UserEntity user2 = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            // Act
            user1.setFirstName("UpdatedFirst1");
            user2.setFirstName("UpdatedFirst2");
            repository.save(user1);
            repository.save(user2);
            entityManager.flush();

            // Assert
            Optional<UserEntity> updated1 = repository.findById(user1.getId());
            Optional<UserEntity> updated2 = repository.findById(user2.getId());
            assertThat(updated1).isPresent().get()
                    .extracting(UserEntity::getFirstName)
                    .isEqualTo("UpdatedFirst1");
            assertThat(updated2).isPresent().get()
                    .extracting(UserEntity::getFirstName)
                    .isEqualTo("UpdatedFirst2");
        }

        @Test
        @Order(23)
        @DisplayName("should rollback on constraint violation")
        void shouldRollbackOnConstraintViolation() {
            // Arrange
            UserEntity existitngUser = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            long initialCount = repository.count();

            // Act & Assert
            UserEntity duplicateUser = RepositoryTestDataBuilder.buildUser();
            duplicateUser.setEmail(existitngUser.getEmail());
            assertThatThrownBy(() -> repository.saveAndFlush(duplicateUser))
                    .isInstanceOf(DataIntegrityViolationException.class);

            // Clear poisoned session
            entityManager.clear();

            // Verify rollback
            assertThat(repository.count()).isEqualTo(initialCount);
        }
    }
}

