---
description: "This files contains instructions for testing design and usage in Java projects. It provides guidelines on how to create and maintain tests that verify the behavior of the application, ensuring they are meaningful, consistent, and easy to maintain."
applyTo: "'**/*Test.java'"
---

# Testing instructions

## General approach

- Prefer tests that verify behavior rather than implementation details.
- Keep unit tests fast and focused.
- Add integration tests for persistence, query logic, and critical service flows.
- Cover edge cases, validation failures, and error paths.
- Avoid brittle tests that depend on incidental details.
- Use descriptive test names that explain the scenario and expectation.

## Test type selection

- Use controller tests for web mapping, validation, and response contracts.
- Use service tests for business rules and orchestration.
- Use repository tests for persistence behavior and query correctness.
- Use integration tests for flows that need the full stack or real database behavior.

## Tooling guidance

- Use Mockito only where mocking improves isolation and readability.
- Prefer small test fixtures over large shared setups.
- Use Testcontainers for PostgreSQL-backed integration tests when the repository already supports it or when Postgres
  behavior matters.
- Keep test data close to the test that uses it.
- Do not over-mock simple collaborators.
- Use project's existing test utilities (`common/**/utils/test/*.java`).

## Change discipline

- When changing behavior, update or add tests in the same change.
- If a bug is fixed, add a regression test that fails before the fix and passes after it.

## Service and Mappers tests

Code example:

```java

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;
    @InjectMocks
    private PriceService priceService;

    @Test
    void shouldComputeFinalPrice() {
        // Custom test data setup
        Long priceId = 1L;

        // Mocks
        when(priceRepository.findById(priceId)).thenReturn(Optional.of(new Price(1L, 666.66)));

        // Test
        double finalPrice = priceService.computeFinalPrice(priceId);

        // Assertions
        assertEquals(666.66, finalPrice);

        // Verify
        verify(priceRepository).findById(priceId);
    }
}
```

- Use @ExtendWith(MockitoExtension.class) for service tests that need mocking.
- Keep service tests focused on the service's behavior and not on the details of its dependencies.
- Avoid testing private methods directly; test them through the public API of the service.
- Use parameterized tests when the same logic needs to be tested with multiple inputs and expected outputs.
- Always verify the expected interactions with mocked dependencies when using Mockito.
- Use assertThrows to verify that exceptions are thrown when expected, verify message and status.

## Repository tests

```java

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindCustomerByEmail() {
        // Custom setup
        Customer customer = Customer.builder()
                .email("john.doe@test.com")
                .build();

        entityManager.persistAndFlush(customer);

        // Test
        Optional<Customer> result =
                repository.findByEmail("john.doe@test.com");

        // Assertions
        assertThat(result).isPresent();
    }
}
```

- For persistence logic, use @DataJpaTest.
- This is the right place for query methods, mappings, custom repository implementations, and Flyway-backed schema
  checks when needed.
- A repository test should test:
    - JPA mappings
    - repository queries
    - paging/sorting behavior
    - transaction boundaries at persistence level

## Controller tests

Code example:

```java

@Transactional
class ControllerCreateTest extends RecipeBaseIntegrationTest {
    @Test
    @Order(1)
    @Transactional
    void createRecipe() throws Exception {
        String req = IntegrationTestUtils.toJson(TestUtils.buildRequest(1L));

        mockMvc.perform(post("/api/path")
                        .header("Authorization",
                                "Bearer " + customToken)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isCreated());
    }
}
```

- Use @Springboot for controller tests that need full application context.
- Use @WebMvcTest for controller tests when possible.
- All controller test classes should:
    - extend RecipeBaseIntegrationTest to ensure consistent test configuration and utilities.
    - be Transactional to ensure test isolation and automatic rollback after each test method.