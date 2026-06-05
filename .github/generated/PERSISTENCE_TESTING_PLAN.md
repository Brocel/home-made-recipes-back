# Persistence Layer Testing - Implementation Plan

**Date:** June 5, 2026  
**Status:** Ready for Implementation  
**Scope:** Comprehensive testing of persistence module with Querydsl predicates

---

## 1. Overview & Goals

### Objective

Establish a comprehensive test suite for the persistence layer ensuring:

- Data integrity and CRUD correctness
- Query accuracy and relationship management
- Transaction consistency and isolation
- Constraint validation and error handling

### Test Coverage Scope

- ✅ **CRUD Operations** for all 6 entities (UserEntity, RecipeEntity, ProductEntity, IngredientEntity, StepEntity,
  RoleEntity)
- ✅ **Custom Derived Queries** (findByEmail, findByNormalizedName, findByName, etc.)
- ✅ **Querydsl Predicates** for advanced filtering and complex queries
- ✅ **Entity Relationships** (cascade operations, orphan removal, lazy loading)
- ✅ **Transaction Boundaries** and isolation verification
- ✅ **Constraint Validation** and unique/required field enforcement

### Expected Coverage

- **Total Test Classes:** 11
- **Total Test Methods:** ~130+
- **Repository Tests:** 88+ methods across 6 repository classes
- **Predicate Tests:** 52+ methods across 5 predicate test classes
- **Execution Time:** < 30 seconds locally

---

## 2. Module Organization & Structure

### Test Directory Structure

```
persistence/src/test/java/com/example/hmrback/persistence/
├── repository/
│   ├── UserRepositoryTest.java              (24 test methods)
│   ├── RecipeRepositoryTest.java            (22 test methods)
│   ├── ProductRepositoryTest.java           (18 test methods)
│   ├── IngredientRepositoryTest.java        (16 test methods)
│   ├── StepRepositoryTest.java              (14 test methods)
│   └── RoleRepositoryTest.java              (14 test methods)
└── util/
    └── RepositoryTestDataBuilder.java       (New helper class)
```

```
common/src/test/java/com/example/hmrback/persistence/predicate/
├── UserPredicateTest.java                   (12 test methods)
├── RecipePredicateTest.java                 (14 test methods)
├── ProductPredicateTest.java                (10 test methods)
├── IngredientPredicateTest.java             (8 test methods)
└── RolePredicateTest.java                   (8 test methods)
```

### Rationale for Module Placement

- **Repository Tests** → `persistence/src/test/` : Tests repository implementations directly
- **Predicate Tests** → `common/src/test/` : Predicates are query-building utilities, shared across modules; common is
  the natural home for shared query construction logic

---

## 3. Repository Tests - Detailed Coverage

### 3.1 UserRepositoryTest.java (24 methods)

#### CRUD Operations (6 methods)

- `shouldSaveNewUserWithRoles()` - Save user with role assignments
- `shouldUpdateUserProfileData()` - Modify existing user attributes
- `shouldDeleteUserById()` - Remove user from database
- `shouldFindUserById()` - Retrieve by UUID primary key
- `shouldFindAllUsersPaginated()` - List all with pagination
- `shouldCascadeDeleteRecipes()` - Verify orphan removal of user's recipes

#### Custom Query Methods (8 methods)

- `shouldFindUserByEmail_WhenExists()` - Query by email, result found
- `shouldReturnEmpty_WhenFindByEmailNotExists()` - Query by email, no result
- `shouldReturnTrue_WhenEmailExists()` - Existence check by email
- `shouldReturnFalse_WhenEmailNotExists()` - Existence check, email absent
- `shouldFindUserByUsername_WhenExists()` - Query by username
- `shouldReturnEmpty_WhenFindByUsernameNotExists()` - Username not found
- `shouldReturnTrue_WhenUsernameExists()` - Existence check by username
- `shouldReturnFalse_WhenUsernameNotExists()` - Existence check, username absent

#### Constraint Validation (4 methods)

- `shouldThrowDataIntegrityViolation_WhenDuplicateUsername()` - Unique constraint
- `shouldThrowDataIntegrityViolation_WhenDuplicateEmail()` - Unique constraint
- `shouldThrowDataIntegrityViolation_WhenMissingFirstName()` - NOT NULL constraint
- `shouldGenerateValidUUID_OnUserCreation()` - UUID generation strategy

#### Relationship Tests (4 methods)

- `shouldSaveUserWithRoles()` - Persist many-to-many USER_ROLE relationships
- `shouldVerifyManyToManyRelationship()` - Confirm junction table entries
- `shouldUpdateUserRoles()` - Modify role associations
- `shouldLazilyLoadRecipes_WhenAccessingRecipeList()` - Lazy loading behavior

#### Transaction Tests (2 methods)

- `shouldVerifyTransactionIsolation_WhenConcurrentUpdates()` - Isolation levels
- `shouldRollbackOnConstraintViolation()` - Transaction rollback behavior

---

### 3.2 RecipeRepositoryTest.java (22 methods)

#### CRUD Operations (5 methods)

- `shouldSaveRecipeWithAuthor()` - Persist recipe with user relationship
- `shouldUpdateRecipeMetadata()` - Modify title, description, type
- `shouldDeleteRecipe_AndOrphanIngredientsAndSteps()` - Cascade delete behavior
- `shouldFindRecipeById_WithLazyLoadedRelationships()` - Retrieve by ID
- `shouldFindRecipesByAuthorId()` - Query by author relationship

#### Custom Query Methods (6 methods)

- `shouldFindAllWithPredicateAndSort()` - Sorted filtered queries
- `shouldFindRecipesByType()` - Filter by RecipeType enum
- `shouldFindRecipesByPublicationDateRange()` - Date-based filtering
- `shouldFindRecipesByAuthor()` - Author relationship queries
- `shouldReturnEmpty_WhenNoMatchingCriteria()` - Empty result handling
- `shouldHandleNullPredicates_Gracefully()` - Null-safe querying

#### Relationship Tests (7 methods)

- `shouldCascadeSave_IngredientsWithRecipe()` - Save ingredients via recipe
- `shouldCascadeSave_StepsWithRecipe()` - Save steps via recipe
- `shouldRemoveOrphans_WhenRemovingIngredients()` - Orphan removal for ingredients
- `shouldRemoveOrphans_WhenRemovingSteps()` - Orphan removal for steps
- `shouldLazilyLoadAuthor_WhenAccessingUserEntity()` - User lazy loading
- `shouldVerifyOneToManyRelationships()` - Relationship verification
- `shouldUpdateMultipleRelationships()` - Update cascading

#### Transaction Tests (4 methods)

- `shouldFlushChangesWithinTransaction()` - Flush behavior verification
- `shouldThrowLazyInitializationException_WhenAccessingOutsideTransaction()` - Lazy loading boundaries
- `shouldIsolateConcurrentRecipeUpdates()` - Transaction isolation
- `shouldRollbackOnForeignKeyViolation()` - FK constraint enforcement

---

### 3.3 ProductRepositoryTest.java (18 methods)

#### CRUD Operations (5 methods)

- `shouldSaveProductWithTypeAndNormalizedName()` - Full persistence
- `shouldUpdateProductName()` - Update single product field
- `shouldDeleteProduct()` - Remove from database
- `shouldFindProductById()` - Retrieve by ID
- `shouldFindAllProducts()` - List all with pagination

#### Custom Query Methods (5 methods)

- `shouldFindByNormalizedName_WhenExists()` - Normalized name lookup
- `shouldReturnEmpty_WhenFindByNormalizedNameNotExists()` - Missing normalized name
- `shouldReturnTrue_WhenNormalizedNameExists()` - Existence check
- `shouldReturnFalse_WhenNormalizedNameNotExists()` - Absence verification
- `shouldFindProductsByIngredientType()` - Filter by type enum

#### Constraint & Validation (4 methods)

- `shouldValidateIngredientTypeEnum()` - Enum mapping validation
- `shouldThrowDataIntegrityViolation_WhenNameRequired()` - NOT NULL constraint
- `shouldVerifyNormalizedNameTriggerBehavior()` - Database trigger side effects
- `shouldHandleSpecialCharactersInProductName()` - Character encoding

#### Transaction Tests (4 methods)

- `shouldMaintainTransactionConsistency()` - Consistency verification
- `shouldRollbackOnTypeViolation()` - Type constraint rollback
- `shouldHandleConcurrentProductNameUpdates()` - Concurrent modification
- `shouldVerifyFK_WithIngredientTable()` - Foreign key integrity

---

### 3.4 IngredientRepositoryTest.java (16 methods)

#### CRUD Operations (4 methods)

- `shouldSaveIngredientWithRecipeAndProductRelationships()` - Full entity persistence
- `shouldUpdateIngredientQuantityAndUnit()` - Modify ingredient attributes
- `shouldDeleteIngredient()` - Remove from database
- `shouldFindIngredientById()` - Retrieve by ID

#### Query Operations (5 methods)

- `shouldFindAllIngredientsForRecipe()` - Recipe-based filtering
- `shouldFindIngredientsByUnitType()` - Filter by Unit enum
- `shouldFindIngredientsByProductId()` - Product relationship query
- `shouldVerifyForeignKeyConstraints()` - Constraint verification
- `shouldReturnEmpty_WhenRecipeHasNoIngredients()` - Empty collections

#### Relationship Tests (4 methods)

- `shouldVerifyManyToOneWithRecipe()` - Recipe relationship
- `shouldVerifyManyToOneWithProduct()` - Product relationship
- `shouldLazilyLoadRecipeAndProduct()` - Lazy loading behavior
- `shouldUpdateIngredientRelationships()` - Relationship updates

#### Transaction Tests (3 methods)

- `shouldHandleOrphanRemoval_WhenIngredientDeleted()` - Orphan behavior
- `shouldCascadeSave_OnRecipePersistence()` - Cascade verification
- `shouldMaintainForeignKeyIntegrity()` - Constraint enforcement

---

### 3.5 StepRepositoryTest.java (14 methods)

#### CRUD Operations (4 methods)

- `shouldSaveStepWithRecipeRelationship()` - Persist step with recipe
- `shouldUpdateStepDescriptionAndOrder()` - Modify step attributes
- `shouldDeleteStep()` - Remove from database
- `shouldFindStepById()` - Retrieve by ID

#### Query Operations (4 methods)

- `shouldFindAllStepsForRecipeOrderedByOrder()` - Recipe filtering with sorting
- `shouldFindStepByOrderSequence()` - Sequential ordering query
- `shouldVerifyForeignKeyConstraints()` - Constraint verification
- `shouldReturnEmpty_WhenRecipeHasNoSteps()` - Empty collections

#### Relationship Tests (3 methods)

- `shouldVerifyManyToOneWithRecipe()` - Recipe relationship
- `shouldLazilyLoadRecipe_WhenAccessingRecipeEntity()` - Lazy loading
- `shouldUpdateStepOrderSequence()` - Sequential updates

#### Transaction Tests (3 methods)

- `shouldMaintainStepOrderConsistency()` - Order field integrity
- `shouldCascadeDeleteSteps_WithRecipe()` - Cascade delete verification
- `shouldHandleConcurrentStepUpdates()` - Concurrent modification safety

---

### 3.6 RoleRepositoryTest.java (14 methods)

#### CRUD Operations (4 methods)

- `shouldSaveRole()` - Persist role entity
- `shouldUpdateRole()` - Modify role if applicable
- `shouldDeleteRole()` - Remove from database
- `shouldFindRoleById()` - Retrieve by ID

#### Custom Query Methods (5 methods)

- `shouldFindByName_ROLE_USER()` - Specific role lookup
- `shouldFindByName_ROLE_ADMIN()` - Specific role lookup
- `shouldFindAllByNameList_WhenMultipleRoles()` - Batch role lookup
- `shouldReturnEmpty_WhenRoleNotFound()` - Role absence handling
- `shouldVerifyEnumMapping()` - RoleEnum mapping validation

#### Constraint & Validation (3 methods)

- `shouldThrowDataIntegrityViolation_WhenDuplicateRoleName()` - Unique constraint
- `shouldThrowDataIntegrityViolation_WhenNameRequired()` - NOT NULL constraint
- `shouldValidateRoleEnumType()` - Enum validation

#### Transaction Tests (2 methods)

- `shouldHandleUserRoleRelationshipCleanup()` - Many-to-many cleanup
- `shouldVerifyCascadeDelete_InUserRoleJunction()` - Cascade behavior

---

## 4. Querydsl Predicate Tests (in common module)

### Location: `common/src/test/java/com/example/hmrback/persistence/predicate/`

### 4.1 UserPredicateTest.java (12 methods)

```
Filter Scenarios:
├── String Matching
│   ├── shouldFilterByFirstNameStartingWith()
│   ├── shouldFilterByFirstNameContaining()
│   ├── shouldFilterByEmailPattern()
│   └── shouldFilterByUsernameLike()
├── Date Range Filtering
│   ├── shouldFilterByBirthDateRange()
│   ├── shouldFilterByInscriptionDateAfter()
│   └── shouldFilterByInscriptionDateBefore()
├── Relationship Filtering
│   └── shouldFilterBySpecificRoles()
├── Complex Predicates
│   ├── shouldCombineMultiplePredicates_WithAND()
│   ├── shouldCombineMultiplePredicates_WithOR()
│   └── shouldHandleNullValues_InPredicates()
└── Sorting & Pagination
    ├── shouldSortByMultipleFields()
    └── shouldPaginateWithAdvancedFilters()
```

---

### 4.2 RecipePredicateTest.java (14 methods)

```
Filter Scenarios:
├── String Matching
│   ├── shouldFilterByTitleContaining()
│   ├── shouldFilterByTitleStartingWith()
│   └── shouldFilterByDescriptionLike()
├── Enum Filtering
│   ├── shouldFilterBySingleRecipeType()
│   ├── shouldFilterByMultipleRecipeTypes()
│   └── shouldHandleRecipeTypeNullValues()
├── Numeric & Date Range
│   ├── shouldFilterByPreparationTimeLessThan()
│   ├── shouldFilterByPreparationTimeRange()
│   ├── shouldFilterByPublicationDateRange()
│   └── shouldFilterByDateBeforeAndAfter()
├── Relationship Filtering
│   ├── shouldFilterByAuthorId()
│   └── shouldFilterByIngredientType()
└── Complex Predicates
    ├── shouldCombineMultiplePredicates_WithComplexAND_OR()
    ├── shouldSortByMultipleFields_WithFiltering()
    ├── shouldPaginateComplexFilteredResults()
    └── shouldHandleNullAndEmptyValues()
```

---

### 4.3 ProductPredicateTest.java (10 methods)

```
Filter Scenarios:
├── Enum Filtering
│   ├── shouldFilterBySingleIngredientType()
│   └── shouldFilterByMultipleIngredientTypes()
├── String Matching
│   ├── shouldFilterByNamePattern()
│   └── shouldFilterByNormalizedNameExact()
├── Sorting
│   ├── shouldSortByNameAscending()
│   ├── shouldSortByTypeDescending()
│   └── shouldSortByMultipleFields()
├── Pagination
│   ├── shouldPaginateWithFiltering()
│   └── shouldHandleEmptyResults()
```

---

### 4.4 IngredientPredicateTest.java (8 methods)

```
Filter Scenarios:
├── Relationship Filtering
│   ├── shouldFilterByRecipeId()
│   └── shouldFilterByProductId()
├── Enum Filtering
│   └── shouldFilterByUnitType()
├── Numeric Range
│   ├── shouldFilterByQuantityGreaterThan()
│   └── shouldFilterByQuantityRange()
└── Complex Predicates
    ├── shouldCombineRecipeAndProductFilters()
    └── shouldHandleNullQuantityValues()
```

---

### 4.5 RolePredicateTest.java (8 methods)

```
Filter Scenarios:
├── Enum Filtering
│   ├── shouldFilterBySingleRoleName()
│   └── shouldFilterByMultipleRoleNames()
├── Sorting
│   ├── shouldSortByRoleNameAscending()
│   └── shouldSortByRoleNameDescending()
├── Pagination
│   └── shouldPaginateRoleResults()
└── Edge Cases
    └── shouldHandleNullOrMissingRoles()
```

---

## 5. Test Utilities & Data Builders

### 5.1 Extend EntityTestUtils (in common/src/main/java)

Add repository-focused builder methods:

```java
// New methods to add to EntityTestUtils
public static RecipeEntity buildRecipeEntityWithIngredients(int ingredientCount)

public static RecipeEntity buildRecipeEntityWithSteps(int stepCount)

public static RecipeEntity buildCompleteRecipe(UserEntity author,
                                               List<ProductEntity> products,
                                               int stepCount)

public static ProductEntity buildProductWithNormalizedName(String name)

public static UserEntity buildUserWithRoles(Set<RoleEntity> roles)

public static IngredientEntity buildIngredientWithQuantity(Double quantity, Unit unit)

public static List<UserEntity> buildUserSetWithVariations(int count)

public static List<RecipeEntity> buildRecipeDataSet(UserEntity author, int count)
```

### 5.2 New: RepositoryTestDataBuilder.java (in persistence/src/test/java)

Purpose: Complex, transactional test data construction for repository-specific scenarios

```java
public class RepositoryTestDataBuilder {

    // Data set builders with relationships
    public static UserDataSet buildUserDataSet()

    public static RecipeDataSet buildRecipeDataSet()

    public static ProductDataSet buildProductDataSet()

    // Constraint violation scenario builders
    public static UserEntity buildUserForConstraintViolation()

    public static ProductEntity buildProductWithDuplicateNormalizedName()

    // Transaction scenario builders
    public static RecipeEntity buildRecipeForCascadeDelete()

    public static IngredientEntity buildIngredientForOrphanRemoval()
}
```

---

## 6. Test Configuration & Setup

### 6.1 Persistence Module Test Configuration

#### Add to `persistence/pom.xml`:

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-test-autoconfigure</artifactId>
<scope>test</scope>
</dependency>
```

#### Test Annotations & Configuration:

```java

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;
}
```

#### Test Profile (application-test.yaml):

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        jdbc:
          batch_size: 20
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
```

### 6.2 Common Module Test Configuration

#### Test Profile for Predicate Tests (application-test.yaml):

```yaml
# Reuse persistence configuration
# Predicates are pure query builders - minimal additional config needed
```

---

## 7. Implementation Phases

### Phase 1: Foundation (3-4 hours)

**Objectives:**

- Set up test infrastructure
- Create test utilities
- Implement simple repository tests

**Deliverables:**

1. Update `persistence/pom.xml` with test dependencies
2. Create `RepositoryTestDataBuilder.java`
3. Extend `EntityTestUtils.java` with repository-specific builders
4. Create `RoleRepositoryTest.java` (simplest entity - 14 methods)
5. Verify test infrastructure works end-to-end

**Acceptance Criteria:**

- RoleRepositoryTest executes successfully with all 14 tests passing
- H2 database initializes correctly for tests
- TestEntityManager works as expected

---

### Phase 2: Core Repository Tests (6-8 hours)

**Objectives:**

- Implement comprehensive repository tests for main entities
- Test CRUD, custom queries, and relationships

**Deliverables:**

1. Create `ProductRepositoryTest.java` (18 methods)
2. Create `UserRepositoryTest.java` (24 methods)
3. Create `RecipeRepositoryTest.java` (22 methods)
4. Create `IngredientRepositoryTest.java` (16 methods)
5. Create `StepRepositoryTest.java` (14 methods)
6. Verify all 88+ methods pass
7. Validate relationship cascade behavior
8. Test constraint violations with proper exception handling

**Acceptance Criteria:**

- All 88+ repository test methods pass
- 100% code coverage for repository interfaces
- Cascade operations verified (save, update, delete)
- FK and unique constraints properly tested
- Transaction isolation tests pass

---

### Phase 3: Querydsl Predicate Tests (4-6 hours)

**Objectives:**

- Implement comprehensive predicate tests in common module
- Test filtering, sorting, pagination with Querydsl

**Deliverables:**

1. Create `common/src/test/java/com/example/hmrback/persistence/predicate/`
2. Create `UserPredicateTest.java` (12 methods)
3. Create `RecipePredicateTest.java` (14 methods)
4. Create `ProductPredicateTest.java` (10 methods)
5. Create `IngredientPredicateTest.java` (8 methods)
6. Create `RolePredicateTest.java` (8 methods)
7. Verify all 52+ predicate tests pass
8. Test complex AND/OR combinations
9. Validate sorting and pagination with predicates

**Acceptance Criteria:**

- All 52+ predicate test methods pass
- Complex filtering scenarios covered
- Sorting and pagination with predicates verified
- Null/empty value handling validated
- Edge cases in predicate construction tested

---

### Phase 4: Integration & Documentation (1-2 hours)

**Objectives:**

- Validate all tests work together
- Document patterns and usage
- Prepare for CI/CD integration

**Deliverables:**

1. Run full test suite: `mvn clean test`
2. Verify execution time < 30 seconds
3. Create test documentation in README
4. Add JavaDoc to all test classes with examples
5. Document test data builders
6. Create troubleshooting guide for common issues

**Acceptance Criteria:**

- Full test suite passes: `mvn clean test` (all modules)
- Execution time < 30 seconds
- Test documentation complete
- No test flakiness or intermittent failures
- Tests pass in CI/CD environment

---

## 8. Testing Patterns & Best Practices

### Pattern 1: Repository CRUD Test Structure

```java

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Nested
    @DisplayName("CRUD Operations")
    class CRUDOperations {

        @Test
        @DisplayName("should save new user with roles")
        void shouldSaveNewUserWithRoles() {
            // Arrange
            UserEntity user = RepositoryTestDataBuilder.buildUserWithRoles(...);

            // Act
            UserEntity saved = repository.save(user);
            entityManager.flush();

            // Assert
            assertThat(saved.getId()).isNotNull();
            assertThat(repository.findById(saved.getId())).isPresent();
        }
    }

    @Nested
    @DisplayName("Custom Query Methods")
    class CustomQueries {

        @Test
        @DisplayName("should find user by email when exists")
        void shouldFindUserByEmail_WhenExists() {
            // Arrange
            UserEntity user = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUser()
            );

            // Act
            Optional<UserEntity> result = repository.findByEmail(user.getEmail());

            // Assert
            assertThat(result).isPresent().get().isEqualTo(user);
        }
    }

    @Nested
    @DisplayName("Constraint Validation")
    class ConstraintValidation {

        @Test
        @DisplayName("should throw constraint violation for duplicate email")
        void shouldThrowDataIntegrityViolation_WhenDuplicateEmail() {
            // Arrange
            String email = "test@example.com";
            UserEntity user1 = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUserWithEmail(email)
            );

            // Act & Assert
            UserEntity user2 = RepositoryTestDataBuilder.buildUserWithEmail(email);
            assertThatThrownBy(() -> {
                repository.save(user2);
                entityManager.flush();
            }).isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @Nested
    @DisplayName("Relationship Management")
    class RelationshipManagement {

        @Test
        @DisplayName("should cascade delete recipes when user deleted")
        void shouldCascadeDeleteRecipes_WhenUserDeleted() {
            // Arrange
            UserEntity user = entityManager.persistAndFlush(
                    RepositoryTestDataBuilder.buildUserWithRecipes(3)
            );
            Long userId = user.getId();

            // Act
            repository.deleteById(userId);
            entityManager.flush();

            // Assert
            assertThat(repository.findById(userId)).isEmpty();
            // Verify recipes were deleted via cascade
        }
    }

    @Nested
    @DisplayName("Transaction Behavior")
    class TransactionBehavior {

        @Test
        @DisplayName("should rollback on constraint violation")
        void shouldRollbackOnConstraintViolation() {
            // Arrange
            long initialCount = repository.count();

            // Act & Assert
            assertThatThrownBy(() -> {
                UserEntity invalidUser = new UserEntity();
                // Missing required fields
                repository.save(invalidUser);
                entityManager.flush();
            }).isInstanceOf(DataIntegrityViolationException.class);

            assertThat(repository.count()).isEqualTo(initialCount);
        }
    }
}
```

### Pattern 2: Querydsl Predicate Test Structure

```java

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RecipePredicateTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecipeRepository repository;

    @BeforeEach
    void setupTestData() {
        // Create test recipes with varying attributes
        entityManager.persistAndFlush(
                RepositoryTestDataBuilder.buildRecipeDataSet()
        );
    }

    @Nested
    @DisplayName("String Matching Predicates")
    class StringMatching {

        @Test
        @DisplayName("should filter by title containing")
        void shouldFilterByTitleContaining() {
            // Arrange
            String searchTerm = "Pasta";

            // Act
            QRecipeEntity recipe = QRecipeEntity.recipeEntity;
            Predicate predicate = recipe.title.containsIgnoreCase(searchTerm);
            List<RecipeEntity> result = repository.findAll(predicate);

            // Assert
            assertThat(result)
                    .isNotEmpty()
                    .allMatch(r -> r.getTitle().toLowerCase().contains(searchTerm.toLowerCase()));
        }
    }

    @Nested
    @DisplayName("Complex Predicates with AND/OR")
    class ComplexPredicates {

        @Test
        @DisplayName("should combine multiple predicates with AND")
        void shouldCombineMultiplePredicates_WithAND() {
            // Arrange
            RecipeType type = RecipeType.MAIN_COURSE;
            int maxPrepTime = 60;

            // Act
            QRecipeEntity recipe = QRecipeEntity.recipeEntity;
            Predicate predicate = recipe.recipeType.eq(type)
                    .and(recipe.preparationTime.loe(maxPrepTime));

            List<RecipeEntity> result = repository.findAll(predicate);

            // Assert
            assertThat(result)
                    .allMatch(r -> r.getRecipeType() == type)
                    .allMatch(r -> r.getPreparationTime() <= maxPrepTime);
        }
    }

    @Nested
    @DisplayName("Sorting with Predicates")
    class SortingWithPredicates {

        @Test
        @DisplayName("should sort by multiple fields while filtering")
        void shouldSortByMultipleFields_WithFiltering() {
            // Arrange
            QRecipeEntity recipe = QRecipeEntity.recipeEntity;
            Predicate predicate = recipe.published.isTrue();

            // Act
            List<RecipeEntity> result = repository.findAll(
                    predicate,
                    Sort.by("recipeType").and(Sort.by("title").ascending())
            );

            // Assert
            assertThat(result).isSortedAccordingTo(
                    Comparator.comparing(RecipeEntity::getRecipeType)
                            .thenComparing(RecipeEntity::getTitle)
            );
        }
    }
}
```

### Pattern 3: Relationship & Cascade Testing

```java

@Test
@DisplayName("should cascade save ingredients when recipe saved")
void shouldCascadeSaveIngredients_WhenRecipeSaved() {
    // Arrange
    UserEntity author = entityManager.persistAndFlush(
            RepositoryTestDataBuilder.buildUser()
    );

    List<ProductEntity> products = entityManager.persistAndFlushAll(
            RepositoryTestDataBuilder.buildProductList(3)
    );

    RecipeEntity recipe = RepositoryTestDataBuilder.buildRecipeWithIngredients(
            author,
            products
    );

    // Act
    RecipeEntity saved = repository.save(recipe);
    entityManager.flush();

    // Assert - Verify ingredients were saved with recipe
    assertThat(saved.getIngredientList())
            .hasSize(3)
            .allMatch(ing -> ing.getId() != null);
}

@Test
@DisplayName("should remove orphans when ingredient removed from recipe")
void shouldRemoveOrphans_WhenIngredientRemoved() {
    // Arrange
    RecipeEntity recipe = entityManager.persistAndFlush(
            RepositoryTestDataBuilder.buildRecipeWithIngredients(3)
    );

    Long ingredientIdToRemove = recipe.getIngredientList().get(0).getId();

    // Act
    recipe.getIngredientList().remove(0);
    repository.save(recipe);
    entityManager.flush();

    // Assert
    assertThat(ingredientRepository.findById(ingredientIdToRemove)).isEmpty();
}
```

---

## 9. Success Criteria & Deliverables

### Test Suite Completeness

- ✅ **88 repository test methods** across 6 test classes
- ✅ **52 predicate test methods** across 5 test classes (in common module)
- ✅ **100% code coverage** for repository interfaces
- ✅ **All entity relationships** verified (cascade, orphan removal, lazy loading)
- ✅ **All custom query methods** tested with edge cases
- ✅ **All constraints** validated with proper exception handling
- ✅ **Transaction boundaries** verified

### Execution Quality

- ✅ **Execution time** < 30 seconds for full suite
- ✅ **No flaky tests** - deterministic and repeatable
- ✅ **H2 compatibility** for CI/CD pipelines
- ✅ **PostgreSQL validation** for production parity

### Documentation Quality

- ✅ **JavaDoc** on all test classes with examples
- ✅ **Test naming** follows `shouldExpectedBehavior_WhenCondition()` pattern
- ✅ **Nested test classes** with @DisplayName for clarity
- ✅ **README documentation** on running tests and extending test suite

### Maintainability

- ✅ **SOLID principle** adherence
- ✅ **Reusable test utilities** in builders
- ✅ **Clear separation** between CRUD, query, and predicate tests
- ✅ **Predicate tests in common module** for shared query logic

---

## 10. Estimated Effort & Timeline

| Phase                          | Duration        | Key Activities                                  |
|--------------------------------|-----------------|-------------------------------------------------|
| **Phase 1: Foundation**        | 3-4 hours       | Infrastructure, utilities, RoleRepositoryTest   |
| **Phase 2: Core Repositories** | 6-8 hours       | 5 main entity repository tests (88 methods)     |
| **Phase 3: Predicates**        | 4-6 hours       | 5 predicate test classes in common (52 methods) |
| **Phase 4: Documentation**     | 1-2 hours       | JavaDoc, README, troubleshooting guide          |
| **TOTAL**                      | **14-20 hours** | Comprehensive persistence layer testing         |

---

## 11. Key Decisions & Rationale

### Decision 1: Predicate Tests in Common Module

**Rationale:** Querydsl predicates are query construction utilities, not persistence-specific implementations. They
belong in the common module as shared query building logic used across services and APIs.

### Decision 2: @DataJpaTest Only (No @SpringBootTest)

**Rationale:** Faster execution, smaller context, focused on persistence layer. Full integration tests remain in the app
module.

### Decision 3: H2 for Tests, PostgreSQL Validation

**Rationale:** H2 provides fast in-memory testing. PostgreSQL-specific behavior can be validated separately or through
integration tests with Testcontainers if needed.

### Decision 4: Nested Test Classes with @Nested

**Rationale:** Improves test organization and readability. Clear separation of concerns (CRUD vs. Query vs.
Relationship).

---

## 12. Getting Started

### Prerequisites

- Maven 3.9+
- Java 21
- Existing project structure in place

### Next Steps

1. Review and approve this plan
2. Proceed with Phase 1: Foundation
3. Implement test utilities and RoleRepositoryTest
4. Gather feedback and adjust patterns if needed
5. Execute Phases 2-4 in sequence

### Support Resources

- Persistence instructions: `.github/instructions/persistence.instructions.md`
- Querydsl guidance: `.github/instructions/querydsl.instructions.md`
- Testing instructions: `.github/instructions/testing.instructions.md`
- Skill: Querydsl Filtering (`.github/skills/querydsl-filtering/`)

---

**Status:** ✅ Ready to implement

