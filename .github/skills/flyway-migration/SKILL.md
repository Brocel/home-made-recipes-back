---
name: flyway-migration
description: Review and design Flyway migrations for schema changes, data backfills, indexes, and constraints. Use this when creating or reviewing database migrations.
---

# Flyway migration skill

When this skill is used, inspect the database change before writing SQL.

## Review checklist

- Decide whether the change is schema-only, data-only, or both.
- Check for production safety.
- Check for large-table lock risk.
- Check nullability, constraints, and indexes.
- Check whether the change should be split into multiple migrations.
- Check rollback implications.
- Check PostgreSQL compatibility.

## Expected behavior

- Keep migrations deterministic and readable.
- Prefer additive changes when possible.
- Separate backfill steps when that reduces risk.
- Avoid hidden or implicit schema behavior.

## Output

- State whether the migration is safe as written.
- List risks or missing steps.
- Suggest the smallest safer migration plan.