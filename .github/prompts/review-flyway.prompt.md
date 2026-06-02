---
agent: 'agent'
name: 'review-flyway'
description: 'Review a Flyway migration for safety and correctness'
---

Review the following Flyway migration.

Focus on:
- schema safety
- data loss risk
- rollback implications
- PostgreSQL compatibility
- indexes and constraints
- whether schema and data changes should be split
- whether the migration is readable and deterministic

Context:
- Related change: ${input:goal:what schema or feature change is being made?}

Migration:
${input:code:paste the migration SQL here}

Return:
1. Safety issues
2. Production risks
3. Suggestions for splitting or improving the migration
4. Test or validation notes