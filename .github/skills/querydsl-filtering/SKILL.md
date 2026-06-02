---
name: querydsl-filtering
description: Build and review Querydsl-based filters, predicates, and projections for backend search use cases. Use this when implementing or reviewing Querydsl code.
---

# Querydsl filtering skill

When this skill is used, focus on complex filtering and search logic.

## Review checklist

- Check predicate composition and readability.
- Check join strategy and fetch strategy.
- Check projection choice.
- Check for N+1 risk.
- Check whether Querydsl is justified over a simpler repository method.
- Check whether the code follows the repository's existing predicate/factory pattern.

## Expected behavior

- Keep predicates explicit and composable.
- Keep query code focused on persistence concerns.
- Prefer maintainable query structures over clever abstractions.
- Prefer the smallest query that satisfies the use case.

## Output

- Explain the query shape.
- Flag performance or correctness risks.
- Suggest a simpler or safer alternative if one exists.