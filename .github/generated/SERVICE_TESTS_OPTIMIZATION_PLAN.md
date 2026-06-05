# Service Layer Tests Optimization - Implementation Plan

**Focus:** Service module only (`service/src/test/java`)  
**Duration:** 2-3 days (4-6 hours)  
**Team:** 1 developer  
**Status:** Ready for implementation

---

## Overview

Optimize service layer tests by:

1. Standardizing assertion messages
2. Removing ReflectionTestUtils anti-pattern
3. Adding parameterized tests for search scenarios
4. Adding transactional behavior verification

**Out of scope (implement later):**

- Predicate factory tests (common module)
- Utility class tests (common module)
- Test builders and boundary value tests

---

## Current State

| File                             | Tests  | Issues                                  |
|----------------------------------|--------|-----------------------------------------|
| `RecipeServiceTest.java`         | 8      | Inconsistent assertion messages         |
| `ProductServiceTest.java`        | 9      | Inconsistent assertion messages         |
| `AuthenticationServiceTest.java` | 8      | Uses ReflectionTestUtils (anti-pattern) |
| **Total**                        | **25** | **3 files need updates**                |

---

## Tasks

### Task 1: Fix AuthenticationServiceTest (1 hour)

**File:** `service/src/test/java/com/example/hmrback/service/auth/AuthenticationServiceTest.java`

**Problem:**

```java

@BeforeEach
void valueSetup() {
    ReflectionTestUtils.setField(service, "adminEmailsRaw", ADMIN_EMAIL);
    ReflectionTestUtils.setField(service, "secretKey", "...");
    ReflectionTestUtils.setField(service, "expirationMinutes", 999L);
}
```

**Solution:** Replace with constructor injection

**Changes:**

1. Find `@BeforeEach void valueSetup()` method
2. Replace entire method with:

```java

@BeforeEach
void setup() {
    service = new AuthenticationService(
            userRepository,
            roleRepository,
            passwordEncoder,
            userMapper,
            "admin@example.com",
            "secret-key-for-tests",
            60L
    );
}
```

3. Delete this import line:

```java
import org.springframework.test.util.ReflectionTestUtils;
```

4. Run tests: `mvn test -pl service -Dtest=AuthenticationServiceTest`

**Validation:**

- [ ] No ReflectionTestUtils usage
- [ ] All 8 tests pass
- [ ] `@BeforeEach` uses constructor injection

---

### Task 2: Add Assertion Messages (45 min)

**Files:**

- `service/src/test/java/com/example/hmrback/service/RecipeServiceTest.java`
- `service/src/test/java/com/example/hmrback/service/ProductServiceTest.java`
- `service/src/test/java/com/example/hmrback/service/auth/AuthenticationServiceTest.java`

**Steps for each file:**

1. Find all assertions WITHOUT messages:
   ```java
   assertEquals(value, result);           // No message
   assertNotNull(result);                 // No message
   assertTrue(condition);                 // No message
   ```

2. Add descriptive messages:
   ```java
   assertEquals(value, result,
           "Should return correct value after operation");
   assertNotNull(result,
           "Should return non-null result");
   assertTrue(condition,
           "Should satisfy condition when precondition met");
   ```

3. Pattern to use:
   ```java
   assertEquals(expected, actual,
           "Should [action] to [expected] when [condition]");
   assertNotNull(result,
           "Should return non-null [type] after [action]");
   ```

**Validation:**

- [ ] All assertions have messages
- [ ] Messages are descriptive
- [ ] All tests still pass

---

### Task 3: Add Parameterized Tests (2 hours)

#### 3a. RecipeServiceTest - Add search scenarios

**File:** `service/src/test/java/com/example/hmrback/service/RecipeServiceTest.java`

**Add imports at top:**

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
```

**Add after existing tests:**

```java

@ParameterizedTest
@MethodSource("provideRecipeFilters")
@Order(99)
void searchRecipes_WithVariousFilters(RecipeFilter filter, int expectedCount) {
    // Setup
    when(repository.findAll(any(Predicate.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(buildRecipeList(expectedCount)));
    when(recipeMapper.toModel(any(RecipeEntity.class))).thenReturn(recipe);

    // Action
    Page<Recipe> result = service.searchRecipes(filter, PageRequest.of(0, 10));

    // Assert
    assertEquals(expectedCount, result.getTotalElements(),
            "Should return correct count for filter type");

    verify(repository, times(1)).findAll(any(Predicate.class), any(Pageable.class));
}

static Stream<Arguments> provideRecipeFilters() {
    return Stream.of(
            Arguments.of(new RecipeFilter(), 0),
            Arguments.of(buildRecipeFilter(RecipeFilterEnum.JUST_NAME, true), 5),
            Arguments.of(buildRecipeFilter(RecipeFilterEnum.DATE_RANGE, true), 3)
    );
}

private static List<RecipeEntity> buildRecipeList(int size) {
    return EntityTestUtils.buildRecipeEntityList(size, false);
}

private static RecipeFilter buildRecipeFilter(RecipeFilterEnum type, boolean active) {
    return CommonTestUtils.buildRecipeFilter(type, active);
}
```

**Validation:**

- [ ] New parameterized test added
- [ ] 3 filter scenarios tested
- [ ] All tests pass

#### 3b. ProductServiceTest - Add search scenarios

**File:** `service/src/test/java/com/example/hmrback/service/ProductServiceTest.java`

**Add same imports as 3a**

**Add after existing tests:**

```java

@ParameterizedTest
@MethodSource("provideProductFilters")
@Order(99)
void searchProducts_WithVariousFilters(ProductFilter filter, int expectedCount) {
    // Setup
    when(repository.findAll(any(Predicate.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(buildProductList(expectedCount)));
    when(productMapper.toModel(any(ProductEntity.class))).thenReturn(product);

    // Action
    Page<Product> result = service.searchProducts(filter, PageRequest.of(0, 10));

    // Assert
    assertEquals(expectedCount, result.getTotalElements(),
            "Should return correct count for filter type");

    verify(repository, times(1)).findAll(any(Predicate.class), any(Pageable.class));
}

static Stream<Arguments> provideProductFilters() {
    return Stream.of(
            Arguments.of(new ProductFilter(), 0),
            Arguments.of(buildProductFilter(ProductFilterEnum.JUST_NAME, true), 5),
            Arguments.of(buildProductFilter(ProductFilterEnum.TYPE, true), 3)
    );
}

private static List<ProductEntity> buildProductList(int size) {
    return EntityTestUtils.buildProductEntityList(size, false);
}

private static ProductFilter buildProductFilter(ProductFilterEnum type, boolean active) {
    return CommonTestUtils.buildProductFilter(type, active);
}
```

**Validation:**

- [ ] New parameterized test added
- [ ] 3 filter scenarios tested
- [ ] All tests pass

#### 3c. AuthenticationServiceTest - Add credential scenarios

**File:** `service/src/test/java/com/example/hmrback/service/auth/AuthenticationServiceTest.java`

**Add same imports as 3a**

**Add after existing tests:**

```java

@ParameterizedTest
@MethodSource("provideValidCredentials")
@Order(99)
void registerUser_WithVariousCredentials(String email, String username, String password) {
    // Setup
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    when(userMapper.toEntity(any(User.class))).thenReturn(userEntity);
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

    // Action
    User result = service.registerUser(new User(email, username, password), "USER");

    // Assert
    assertNotNull(result,
            "Should successfully register user: " + username);

    verify(userRepository, times(1)).save(any(UserEntity.class));
}

static Stream<Arguments> provideValidCredentials() {
    return Stream.of(
            Arguments.of("user1@test.com", "user1", "Password123!"),
            Arguments.of("user2@test.com", "user2", "SecurePass456@"),
            Arguments.of("user3@test.com", "user3", "AnotherPass789#")
    );
}
```

**Validation:**

- [ ] New parameterized test added
- [ ] 3 credential scenarios tested
- [ ] All tests pass

---

### Task 4: Add Transactional Verification (1 hour)

#### 4a. RecipeServiceTest

**File:** `service/src/test/java/com/example/hmrback/service/RecipeServiceTest.java`

**Add after other tests:**

```java

@Test
@Order(10)
void updateRecipe_OnException_ShouldNotSave() {
    // Setup
    when(repository.findById(anyLong())).thenReturn(Optional.of(recipeEntity));
    doThrow(new RuntimeException("Mapping failed"))
            .when(recipeMapper).updateEntityFromModel(any(), any());

    // Action & Assert
    assertThrows(RuntimeException.class,
            () -> service.updateRecipe(1L, recipe),
            "Should throw exception when mapper fails");

    // Verify no save occurred
    verify(repository, times(0)).saveAndFlush(any());
}

@Test
@Order(11)
void deleteRecipe_ShouldRemoveEntity() {
    // Setup
    when(repository.findById(anyLong())).thenReturn(Optional.of(recipeEntity));

    // Action
    service.deleteRecipe(1L);

    // Assert
    verify(repository, times(1)).delete(recipeEntity);
}
```

#### 4b. ProductServiceTest

**File:** `service/src/test/java/com/example/hmrback/service/ProductServiceTest.java`

**Add after other tests:**

```java

@Test
@Order(10)
void updateProduct_OnException_ShouldNotSave() {
    // Setup
    when(repository.findById(anyLong())).thenReturn(Optional.of(productEntity));
    doThrow(new RuntimeException("Mapping failed"))
            .when(productMapper).updateEntityFromModel(any(), any());

    // Action & Assert
    assertThrows(RuntimeException.class,
            () -> service.updateProduct(1L, product),
            "Should throw exception when mapper fails");

    // Verify no save occurred
    verify(repository, times(0)).saveAndFlush(any());
}

@Test
@Order(11)
void deleteProduct_ShouldRemoveEntity() {
    // Setup
    when(repository.findById(anyLong())).thenReturn(Optional.of(productEntity));

    // Action
    service.deleteProduct(1L);

    // Assert
    verify(repository, times(1)).delete(productEntity);
}
```

**Validation:**

- [ ] Exception handling tests added
- [ ] Delete verification tests added
- [ ] All tests pass

---

## Execution Checklist

- [ ] Task 1: AuthenticationServiceTest - Remove ReflectionTestUtils (1 hr)
- [ ] Task 2: Add assertion messages to all 3 tests (45 min)
- [ ] Task 3a: RecipeServiceTest - Add parameterized search (30 min)
- [ ] Task 3b: ProductServiceTest - Add parameterized search (30 min)
- [ ] Task 3c: AuthenticationServiceTest - Add parameterized registration (30 min)
- [ ] Task 4a: RecipeServiceTest - Add transactional tests (30 min)
- [ ] Task 4b: ProductServiceTest - Add transactional tests (30 min)

**Total: 4-6 hours**

---

## Testing After Each Task

```bash
# Full validation
mvn clean test -pl service

# Specific test class
mvn test -pl service -Dtest=RecipeServiceTest

# Exit on failure
mvn test -pl service -DfailIfNoTests=true
```

---

## Final Validation

After all tasks complete:

```bash
# Full test run
mvn clean test -pl service

# Expected results
# - 25 → ~35 tests (added ~10 parameterized + transactional)
# - 0 ReflectionTestUtils usage
# - 100% assertions have messages
# - All tests pass ✓
```

---

## Git Commits

```bash
# Commit 1: Fix ReflectionTestUtils
git add service/src/test/java/com/example/hmrback/service/auth/AuthenticationServiceTest.java
git commit -m "refactor(test): Remove ReflectionTestUtils from AuthenticationServiceTest"

# Commit 2: Add assertion messages
git add service/src/test/java/com/example/hmrback/service/*ServiceTest.java
git commit -m "refactor(test): Standardize assertion messages in service layer tests"

# Commit 3: Add parameterized tests
git add service/src/test/java/com/example/hmrback/service/*ServiceTest.java
git commit -m "refactor(test): Add parameterized search and registration tests"

# Commit 4: Add transactional verification
git add service/src/test/java/com/example/hmrback/service/*ServiceTest.java
git commit -m "refactor(test): Add transactional behavior verification tests"
```

---

## Success Metrics

| Metric                    | Before  | After | Status |
|---------------------------|---------|-------|--------|
| Test Count                | 25      | 35+   | ⬆️     |
| ReflectionTestUtils       | 1 class | 0     | ✅      |
| Assertions with Messages  | ~70%    | 100%  | ✅      |
| Parameterized Tests       | 0       | 9     | ✅      |
| Transactional Tests       | 0       | 4     | ✅      |
| Code Following Guidelines | ~80%    | 100%  | ✅      |

---

## Quick Reference

### Finding what to change in RecipeServiceTest

Search for these patterns and add messages:

```java
// Find:
assertEquals(product, result);

// Replace with:
assertEquals(product, result,
        "Should return product matching input DTO");
```

### Imports needed for parameterized tests

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
```

### Pattern for @MethodSource

```java

@ParameterizedTest
@MethodSource("provideData")
void testName(Param1 p1, Param2 p2) {
    // Test body
}

static Stream<Arguments> provideData() {
    return Stream.of(
            Arguments.of(value1, value2),
            Arguments.of(value3, value4)
    );
}
```

---

## Notes

- Keep it simple - only service module
- No new test files needed
- Modify only 3 existing test files
- All changes fit existing test patterns
- Can be done with standard text editor + Maven

**Ready to implement!** 🚀

