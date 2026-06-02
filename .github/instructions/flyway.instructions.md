# Flyway instructions

## Migration rules

- All schema changes must go through Flyway migrations.
- Do not modify an already-applied migration file unless the branch policy explicitly allows it.
- Prefer one migration per logical change.
- Keep migrations deterministic and reviewable.
- Keep migration SQL readable and minimal.
- Always ask for review on migration files, even for small changes.

## Change design

- Separate schema changes from data backfills when that improves safety.
- Add database constraints when the domain requires them.
- Add indexes only when they support a real query or constraint need.
- Consider large tables, existing data, and production rollout impact before changing schema.
- Keep nullability changes and destructive changes deliberate and well justified.
- Prefer explicit SQL over hidden schema behavior.

## Safety checks

- Think through rollback implications.
- Avoid changes that would unexpectedly lock large tables for too long.
- Prefer additive changes first when the change can be rolled out safely in stages.
- Make the SQL compatible with the target PostgreSQL version used by the project.