---
agent: 'agent'
name: 'review-service'
description: 'Review a service-layer change in the backend'
---

Review the following service-layer code as a Spring Boot backend reviewer.

Focus on:

- business rules and use-case boundaries
- transaction scope
- side effects and consistency
- error handling
- coupling to controllers or repositories
- unnecessary complexity
- testability

Context:

- Module or path: ${input:path:service or use case}
- Desired behavior: ${input:goal:what should this service do?}

Code:
${input:code:paste the code here}

Return:

1. Critical issues
2. Suggested improvements
3. Hidden risks or edge cases
4. Tests to add