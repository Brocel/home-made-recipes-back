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

## Change discipline

- When changing behavior, update or add tests in the same change.
- If a bug is fixed, add a regression test that fails before the fix and passes after it.