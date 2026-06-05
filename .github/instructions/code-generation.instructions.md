---
description: "Guidelines for code generation in Java projects using Copilot"
applyTo: "**/*.java"
---

# Code generation instructions

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

## Enum generation

- Use enums for fixed sets of domain values.
- Keep enum names clear and business-oriented.
- Do not overload enums with unrelated responsibilities.
- Prefer helper methods only when they improve readability and stay simple.

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