---
description: "This file contains instructions for request/response DTOs and domain model design."
applyTo: "'common/**/model/**/*.java'"
---

# DTO and domain model design instructions

## DTO design rules

- Use DTOs for API input and output models.
- Do not expose JPA entities through the API layer.
- Keep DTOs flat unless nesting is truly required.
- Prefer immutable DTOs.
- Use records for simple request/response DTOs when they fit naturally.
- Use validation annotations on input DTOs.
- Keep DTO names descriptive, such as:
    - `CreateRecipeRequest`
    - `UpdateRecipeRequest`
    - `RecipeResponse`
    - `SearchRecipesQuery`
- Do not put business logic in DTOs.

## Domain model design rules

- Prefer records for simple, immutable data carriers.
- Use records when the type is only transporting data and does not need mutable state.
- Keep record components minimal and intentional.
- Do not use records when the type needs lifecycle behavior, mutation, or complex invariants.
- Prefer records for:
    - request/response DTOs
    - simple command objects
    - small projection models
    - value-like return types