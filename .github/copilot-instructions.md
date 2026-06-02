# Repository Copilot Instructions

## Project identity
This repository contains backend services built with:
- Java 21
- Spring Boot 3.5.6
- Microservice architecture
- JPA / Hibernate
- QuerydslPredicateExecutor / Querydsl for complex queries
- Flyway for schema migrations
- PostgreSQL as the database

Treat each microservice as an independently deployable unit with its own domain boundaries, persistence concerns, and runtime configuration.

## Core engineering principles
- Prefer simple, explicit code over clever abstractions.
- Follow existing code style and package structure before introducing new patterns.
- Do not invent frameworks, layers, or conventions that are not already present in the repository.
- When the codebase already has a pattern, extend that pattern rather than creating a competing one.
- Make minimal, safe changes that preserve behavior unless the task explicitly asks for a refactor.
- Keep business logic out of controllers, repositories, and configuration classes.
- Prefer small, cohesive methods with one responsibility.

## Java and Spring conventions
- Use Java 21 features when they improve clarity, but do not overuse novelty.
- Prefer constructor injection for required dependencies.
- Prefer immutable data where practical.
- Prefer records for simple request/response DTOs when they fit the use case.
- Use `@ConfigurationProperties` for structured configuration.
- Use validation annotations on input DTOs.
- Keep `@Controller` and `@RestController` classes thin.
- Keep `@Service` classes focused on orchestration and business rules.
- Keep repository interfaces focused on persistence access only.

## Layering and architecture
Use this default layering unless the repository already uses a different proven structure:

- `api` / `web`: controllers, request/response DTOs, exception handlers, API-facing mapping
- `application`: use cases, orchestration, transactional coordination
- `domain`: domain entities, value objects, domain rules, domain services
- `infrastructure`: persistence implementations, external system adapters, messaging adapters
- `config`: Spring configuration, bean wiring, feature flags, security setup

Rules:
- Do not place business logic in controllers.
- Do not leak JPA entities directly out of the API layer.
- Do not couple external integrations directly to domain objects unless the repository already does so.
- Keep domain logic independent from transport concerns.
- Prefer explicit mapping between API DTOs, application models, and persistence entities.
- Do not create shared mutable state across services.

## Microservice boundaries
- Assume each service owns its own data model.
- Do not share database tables across services.
- Do not couple services through direct database access.
- Prefer APIs, events, or well-defined contracts for inter-service communication.
- Keep shared libraries small and technical; avoid putting business rules into shared modules.
- If a change affects more than one service, identify the owning service first and keep changes isolated.

## REST API design
- Keep endpoints resource-oriented and predictable.
- Use consistent plural nouns for collections unless the existing API uses a different convention.
- Use the correct HTTP methods and status codes.
- Return DTOs, not entities.
- Use pagination for collection endpoints that can grow large.
- Use filtering and sorting explicitly rather than ad hoc query parameters.
- Keep API contracts stable and backward compatible when possible.
- Prefer clear, descriptive endpoint names over generic verbs.

## Validation and error handling
- Validate all external input at the boundary.
- Fail fast on invalid input.
- Return structured error responses with stable fields.
- Prefer a centralized exception handling strategy.
- Map technical exceptions to meaningful API errors.
- Never expose stack traces or internal implementation details in public responses.
- Use consistent error codes/messages across the service.
- Make error responses easy for clients to parse and debug.

## Persistence and JPA / Hibernate
- Keep entity mappings as simple as possible.
- Avoid over-modeling persistence entities.
- Model relationships only when they are truly needed.
- Prefer lazy loading by default, but be explicit about fetch strategy when the use case requires it.
- Avoid N+1 query problems; verify query shape when adding new access paths.
- Use transactions intentionally and with the smallest scope that is correct.
- Do not put business logic inside entities unless the repository already uses rich domain entities in a consistent way.
- Prefer query methods that are readable and maintainable.
- Use Querydsl for complex filtering, joins, projections, and search use cases.
- Do not use Querydsl where a simple derived query or standard repository method is enough.
- Keep repository interfaces focused on persistence access.

## Flyway and database migrations
- All schema changes must go through Flyway migrations.
- Never edit an already-applied migration file unless the task explicitly concerns a clean dev-only branch and the repository conventions allow it.
- Prefer one migration per logical change.
- Keep migrations deterministic and idempotent where appropriate.
- Add database constraints when the domain requires them.
- Keep migration SQL readable and reviewable.
- Consider the impact on large tables, existing data, and rollback strategy.
- When adding columns or indexes, think about production safety and migration cost.

## Testing strategy
- Prefer tests that verify behavior over tests that mirror implementation details.
- Keep unit tests fast and focused.
- Add integration tests for persistence, query logic, and critical service flows.
- Use Testcontainers for PostgreSQL-backed integration tests when the repository already supports it or when it materially improves confidence.
- Cover edge cases, validation, and error paths.
- Avoid brittle tests that depend on incidental details.
- Use descriptive test names that explain the scenario and expectation.
- When changing behavior, update or add tests in the same change.

## Transaction and consistency rules
- Use transactions where atomicity is required.
- Keep transaction boundaries close to the use case.
- Do not hold transactions open longer than needed.
- Be careful with side effects inside transactions.
- Ensure external calls are not accidentally made inside long-running critical sections unless the design requires it.

## Security
- Assume all external input is untrusted.
- Never hardcode secrets.
- Never log credentials, tokens, or sensitive personal data.
- Apply least-privilege principles to service accounts and integrations.
- Validate authorization explicitly at the boundary.
- Treat security-related changes as high priority and high risk.
- Prefer secure defaults in configuration.

## Logging, observability, and operations
- Use structured, meaningful logs.
- Log business-relevant context without leaking sensitive information.
- Prefer correlation identifiers where available.
- Make operational failures easy to diagnose.
- Add metrics or tracing hooks when a change affects a critical path or an external integration.
- Keep startup and health-check behavior predictable.

## Configuration
- Externalize environment-specific values.
- Use typed configuration over scattered string literals.
- Keep defaults safe.
- Separate development, test, and production concerns clearly.
- Avoid configuration duplication across services.

## Code style and readability
- Follow the repository’s existing formatting rules.
- Use meaningful names for packages, classes, methods, and variables.
- Keep methods short and intention-revealing.
- Avoid unnecessary comments; prefer self-explanatory code.
- Add comments only where the reasoning is not obvious from the code.
- Prefer small refactors that improve readability without changing behavior.

## Dependency management
- Do not add a new dependency unless it is clearly justified.
- Prefer existing platform and Spring capabilities first.
- When adding a dependency, prefer the smallest, most stable option.
- Keep versions aligned with the repository’s dependency management strategy.

## Working rules for Copilot
When generating or modifying code:
- Inspect nearby code first and follow the local conventions.
- Prefer the simplest correct implementation.
- Preserve existing public contracts unless a breaking change is requested.
- Do not introduce TODOs as a substitute for implementation.
- Do not remove tests unless they are obsolete and replaced with better coverage.
- If there are multiple valid approaches, choose the one that best matches the existing repository style.
- If requirements are ambiguous, make the smallest reasonable assumption and keep the change reversible.

## When adding new features
- Identify the service boundary first.
- Add or update request/response DTOs.
- Add service logic in the application layer.
- Add or update repository queries only if necessary.
- Add Flyway migrations for schema changes.
- Add tests for the happy path and the important failure paths.
- Keep the API backward compatible unless the change is explicitly breaking.

## What to avoid
- Do not generate frontend code in this backend file.
- Do not create speculative abstractions.
- Do not duplicate business rules across layers.
- Do not bypass validation or security checks.
- Do not use field injection.
- Do not use reflection-heavy patterns unless the repository already depends on them.
- Do not over-engineer for hypothetical future requirements.

## Output expectations
- Make changes that are coherent with the existing repository.
- Prefer code that is easy to review and easy to test.
- Keep diffs focused.
- Explain trade-offs briefly when a design choice matters.