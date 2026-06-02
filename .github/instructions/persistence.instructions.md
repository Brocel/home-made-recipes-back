# Persistence instructions

## Entity modeling

- Keep entity mappings simple.
- Model relationships only when they are truly needed.
- Prefer lazy loading by default.
- Be explicit about fetch strategy when the use case requires it.
- Avoid over-modeling persistence entities.
- Do not put business logic inside entities unless the repository already follows a rich-domain pattern consistently.

## Repository rules

- Keep repository interfaces focused on persistence access only.
- Prefer readable and maintainable query methods.
- Use derived queries when they are clear and sufficient.
- Use custom queries only when they add real value.

## Querydsl rules

- Use Querydsl for complex filtering, joins, projections, and search use cases.
- Prefer Querydsl when the query is known and stable but too complex for a derived method.
- Keep Querydsl code focused on data access concerns.
- Avoid mixing business rules into Querydsl predicates or repositories.
- Prefer explicit projections over broad entity fetching when the use case only needs partial data.
- Avoid reflection-heavy or overly generic query abstractions.
- Reuse the repository's existing predicate/factory pattern if one already exists.

## Performance and consistency

- Avoid N+1 query problems.
- Check query shape when adding new access paths.
- Use transactions intentionally and with the smallest correct scope.
- Keep read and write concerns separated when it makes the design clearer.
- Be careful with cascading operations and orphan removal.
- Make access patterns explicit when performance matters.

## Mapping and boundaries

- Do not let JPA entities leak out through API endpoints.
- Prefer explicit mapping between persistence entities, service models, and DTOs.
- Keep persistence-specific concerns out of API and domain-facing code.