---
agent: 'agent'
name: 'review-persistence'
description: 'Review JPA or Querydsl persistence code'
---

Review the following persistence code as a backend reviewer.

Focus on:

- entity design
- repository design
- Querydsl correctness
- fetch strategy
- N+1 risk
- transaction scope
- mapping and boundary leakage
- query readability and maintainability

Context:

- Module or path: ${input:path:persistence or query class}
- Desired behavior: ${input:goal:what should this persistence code achieve?}

Code:
${input:code:paste the code here}

Return:

1. Critical issues
2. Query or mapping concerns
3. Performance risks
4. Tests to add