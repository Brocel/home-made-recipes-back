# API instructions

## Controller rules

- Keep controllers thin.
- Put business logic in the service layer, not in controllers.
- Do not return JPA entities from controllers.
- Keep controller methods focused on one endpoint and one use case.

## Request and response design

- Use explicit DTOs for requests and responses.
- Validate request DTOs at the boundary.
- Keep response shapes stable and predictable.
- Do not expose internal persistence fields unless the API truly needs them.

## REST rules

- Use resource-oriented endpoint names.
- Use the correct HTTP methods and status codes.
- Prefer plural collection nouns unless the existing API already uses another convention.
- Keep endpoint naming consistent across the service.
- Keep APIs backward compatible when possible.

## Filtering, sorting, and pagination

- Use pagination for large collections.
- Use filtering and sorting explicitly and consistently.
- Keep query parameters predictable and documented.
- Prefer simple request models over ad hoc query parameter combinations.
- If a search endpoint grows complex, move the complexity into the service layer or query builder.

## Validation and errors

- Validate all external input at the boundary.
- Fail fast on invalid input.
- Use a centralized exception handling approach.
- Return structured error responses with stable fields.
- Do not expose stack traces or internal implementation details.
- Keep error messages useful for clients and safe for production.
- Prefer a single API error contract for the service.

## OpenAPI and documentation

- Use OpenAPI to document the API contract.
- Use yaml format for OpenAPI files and keep them in the `app` module.
- Keep OpenAPI yaml aligned with the actual behavior.
- Document validation rules, required fields, and important error responses.
- Keep examples realistic and consistent with the domain.