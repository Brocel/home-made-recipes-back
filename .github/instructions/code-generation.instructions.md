# Code generation instructions

## Scope

Apply these rules whenever Copilot refactors or generates a new Java file or proposes a new type, including:

- classes
- interfaces
- abstract classes
- DTOs
- records
- utilities
- constants
- enums
- mappers
- exception types

## General rules

- Inspect nearby code first and match the repository style.
- Prefer the simplest correct type for the job.
- Do not generate unnecessary abstractions.
- Do not create placeholder code, TODOs, or fake implementations.
- Do not generate duplicate types that already exist in the codebase.
- Keep file names, class names, and package names aligned.
- Prefer the most specific type that solves the problem.
- Do not introduce extra types unless they remove duplication, improve clarity, or enforce a boundary.
- If a simpler type is enough, use it.

## Class generation

- Use a `class` when the type has behavior, state, or orchestration logic.
- Keep classes small and focused on one responsibility.
- Prefer package-private helpers when the type does not need to be public.
- Avoid large “utility” classes that collect unrelated behavior.
- Prefer composition over inheritance unless the existing codebase already uses inheritance for the same concern.

## Interface generation

- Use an `interface` only when it provides a real contract boundary.
- Prefer interfaces for:
    - service contracts
    - adapter ports
    - strategy-like behavior
    - repository abstractions when the project already follows that pattern
- Do not create an interface just because it is possible.
- If there is only one implementation and no contract boundary, prefer a class.

## Abstract class generation

- Use an abstract class only when shared state or shared protected behavior is required.
- Prefer composition or interfaces before abstraction.
- Do not create abstract base classes for convenience alone.
- Keep abstract classes narrow and explicit.
- Do not introduce inheritance hierarchies unless the codebase already supports them.

## DTO generation

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

## Record generation

- Prefer records for simple, immutable data carriers.
- Use records when the type is only transporting data and does not need mutable state.
- Keep record components minimal and intentional.
- Do not use records when the type needs lifecycle behavior, mutation, or complex invariants.
- Prefer records for:
    - request/response DTOs
    - simple command objects
    - small projection models
    - value-like return types

## Utility generation

- Use a utility class only for stateless reusable helpers.
- Utility classes must:
    - be `final`
    - have a private constructor
    - expose only static methods
- Do not create utility classes for business logic.
- Do not use utility classes when a domain service or mapper is more appropriate.
- Group utility methods by concern, not by convenience.

## Constant generation

- Use a dedicated constants class only when a constant is shared and truly reusable.
- Constant classes must:
    - be `final`
    - have a private constructor
    - contain only constants and related static helpers if needed
- Prefer local constants inside the owning class when the value is only used there.
- Use clear constant names in `UPPER_SNAKE_CASE`.
- Do not place unrelated constants in the same class.

## Mapper generation

- Prefer MapStruct for object mapping when the project already uses it.
- Keep mappers focused on translation only.
- Do not embed business logic in mappers.
- Keep mapping names explicit and readable.
- Prefer one mapper per aggregate or feature area when practical.

## Enum generation

- Use enums for fixed sets of domain values.
- Keep enum names clear and business-oriented.
- Do not overload enums with unrelated responsibilities.
- Prefer helper methods only when they improve readability and stay simple.

## Exception generation

- Use specific exception types for specific failure cases.
- Keep exception names descriptive and stable.
- Do not create generic exception wrappers unless the architecture already requires them.
- Include only the context needed to diagnose the problem safely.

## Naming rules

- Use nouns for types and verbs for methods.
- Keep names business-oriented and domain specific.
- Avoid abbreviations unless they are standard in the codebase.
- Match existing package and module naming conventions.
- Keep generated names consistent with the surrounding code.

## Output expectations

- Generate code that compiles cleanly.
- Prefer complete, minimal files over partial scaffolding.
- Include imports only when needed.
- Do not add unrelated helper types.
- Do not generate code that conflicts with existing patterns.