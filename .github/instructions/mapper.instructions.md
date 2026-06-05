---
description: "This file contains instructions for mapper design and usage in Java projects. It provides guidelines on how to create and maintain mappers that convert between different data representations, such as DTOs and domain models."
applyTo: "'common/**/mapper/**/*.java'"
---

# Mapper design instructions

## General rules

- Prefer MapStruct for object mapping when the project already uses it.
- Keep mappers focused on translation only.
- Do not embed business logic in mappers.
- Keep mapping names explicit and readable.
- Prefer one mapper per aggregate or feature area when practical.

## Mapstruct usage rules

- Use Mapstruct only for DTOs -> Entity and Entity -> DTO mappings.
- Avoid using Mapstruct for complex domain model transformations that require business logic.
- Keep Mapstruct mappers simple and focused on field mapping.
- Use Mapstruct's `@Mapping` annotations to handle field name differences and type conversions.
- Do not use Mapstruct for mapping between domain models or for mapping that requires significant logic.
