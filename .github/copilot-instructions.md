# GitHub Copilot Instructions - Home Made Recipes Application

## Project identity

This repository contains the backend services for the Home Made Recipes platform.

The application lets users browse, search, create, edit, share recipes, and plan weekly menus.

User groups:

- Guest users can browse, search, and view recipes.
- Registered users can manage recipes, products, meal plans, and profile preferences.

Main business domains:

- Dashboard: personalized summary, weekly menu, profile shortcuts, and useful insights.
- Recipes: browse, search, filter, view, create, edit, and share recipes.
- Products: browse, search, filter, view, and add ingredients that can be used in recipes.
- Planner: weekly meal planning and shopping list generation.
- Profile: edit profile data, manage preferences, and navigate to the dashboard.
- Login and registration: user authentication and account management.

## Stack

- Maven
- Java 21
- Spring Boot 3.5.6
- JJWT 0.12.5
- MapStruct 1.6.0
- Lombok 1.18.32
- Lombok MapStruct Binding 0.2.0
- Querydsl 5.0.0
- H2 2.1.214 for tests
- PostgreSQL 42.6.0
- Swagger / OpenAPI 2.8.5
- JPA / Hibernate
- Flyway 11.7.2
- JUnit 5
- Mockito

## Repository structure

The repository contains multiple microservices. Each service owns its own domain, persistence model, and deployment
boundary.

- `api`: REST controllers, and API-facing mapping.
- `app`: application bootstrap, configuration, global exception handling, and resources such as migrations and OpenAPI
  files.
- `common`: shared technical code such as utilities, exceptions, mappers, models, predicates, and validators.
- `persistence`: JPA entities, repositories, and persistence enums.
- `service`: business logic and use-case orchestration.

## Global engineering rules

- Prefer readability, maintainability, explicitness, and consistency over clever abstractions.
- Follow existing code style and patterns in the repository before introducing new ones.
- Extend existing conventions instead of creating competing ones.
- Make minimal, safe changes that preserve behavior unless the task explicitly asks for a refactor.
- Keep business logic out of controllers, repositories, and configuration classes.
- Keep methods small, cohesive, and intention-revealing.
- Prefer simple, explicit code over advanced patterns when both are valid.
- Assume each microservice is independently deployable and should not depend on another service's internals.
- Use most recent Java 21 best practices.
- Evaluate design patterns when a concrete problem exists.
- Prefer the simplest solution that satisfies current requirements.
- Do not introduce patterns without clear justification.
- Prefer creating new classes over writing nested classes.
- If a bug is fixed, add a regression test that fails before the fix and passes after it.

## Java and Spring rules

- Prefer constructor injection for required dependencies.
- Use Java 21 features only when they improve clarity.
- Prefer immutable data where practical.
- Prefer records for simple request and response DTOs when they fit the use case.
- Keep controllers thin.
- Keep services focused on orchestration and business rules.
- Keep repositories focused on persistence access only.
- Use `@ConfigurationProperties` for structured configuration.
- Use Bean Validation at the API boundary.

## Working rules for Copilot

- Inspect nearby code first and follow local conventions.
- Prefer the simplest correct implementation.
- Preserve existing public contracts unless a breaking change is requested.
- Do not introduce TODOs as a substitute for implementation.
- Do not remove tests unless they are obsolete and replaced with better coverage.
- If there are multiple valid approaches, choose the one that best matches the existing repository style.
- If requirements are ambiguous, make the smallest reasonable assumption and keep the change reversible.

## Markdown artifact policy

- Do not create markdown files in the repository root.
- Do not create report, plan, review, or completion markdown files in feature folders.
- If a markdown artifact is needed, place it only under `.github/generated/`.
- If the destination folder does not exist, ask before creating a new one.

## What to avoid

- Do not generate frontend code in this backend repository.
- Do not create speculative abstractions.
- Do not duplicate business rules across layers.
- Do not bypass validation or security checks.
- Do not use field injection.
- Do not use reflection-heavy patterns unless the repository already depends on them.
- Do not over-engineer for hypothetical future requirements.
- Do not commit anything.
- Do not execute any command in terminal