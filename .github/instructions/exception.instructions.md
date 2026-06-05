---
description: "This file contains instructions for Exception design and usage in Java projects. It provides guidelines on how to create and maintain exceptions that represent error conditions in the application, ensuring they are meaningful, consistent, and easy to use."
applyTo: "'common/**/exception/**/*.java', 'service/**/service/**/*.java', 'api/**/controller/**/*.java'"
---

# Exception design instructions

## General rules

- Use specific exception types for specific failure cases.
- Keep exception names descriptive and stable.
- Do not create generic exception wrappers unless the architecture already requires them.
- Include only the context needed to diagnose the problem safely.
- Avoid including sensitive information in exception messages or fields.
- Use the GlobalExceptionHandler for consistent API error responses.
- Use the existing Exception hierarchy in the repository when possible, and extend it for new cases.