---
description: "This file contains instructions for using the OpenAPI specification to define and document RESTful APIs. It provides guidelines on how to create and maintain an OpenAPI document, which serves as a blueprint for API development and integration."
applyTo: "app/**/openapi/**/*.yaml"
---

# OpenAPI specification instructions

## General rules

- Use openapi 3.0.3 for API documentation.
- Keep the OpenAPI document up-to-date with the actual API implementation.
- Use descriptive titles, summaries, and descriptions for API endpoints, parameters, errors and responses.
- Define reusable components for schemas, parameters, and responses to promote consistency and reduce duplication.
- Put reusable components in `**/openapi/schemas` folder and reference them in the main OpenAPI document.
- Use appropriate HTTP status codes for responses and document them clearly.
- Include examples for request bodies and responses to clarify expected data formats.
- Use tags to group related endpoints and improve API discoverability.