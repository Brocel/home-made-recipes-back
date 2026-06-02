---
name: rest-api-review
description: Review REST controllers, request DTOs, and API contracts for correctness, clarity, and backward compatibility. Use this when reviewing endpoints, controllers, or request/response designs.
---

# REST API review skill

When this skill is used, inspect the endpoint from the API boundary inward.

## Review checklist

- Check HTTP method, path, and status codes.
- Check request and response DTO design.
- Check validation and error handling.
- Check pagination, filtering, and sorting.
- Check whether the controller leaks persistence concerns.
- Check compatibility with existing API contracts.
- Check OpenAPI alignment when relevant.

## Expected behavior

- Prefer thin controllers.
- Prefer explicit mapping.
- Prefer stable response shapes.
- Prefer minimal, safe changes.

## Output

- List the main issues first.
- Explain the impact of each issue.
- End with the smallest safe recommendation.