---
agent: 'agent'
name: 'review-api'
description: 'Review a REST endpoint change in the backend'
---

Review the following REST API code as a Spring Boot backend reviewer.

Focus on:
- endpoint naming and HTTP semantics
- DTO design and mapping
- validation and error handling
- backward compatibility
- pagination, filtering, and sorting
- leaking entities or persistence concerns
- OpenAPI alignment if relevant

Context:
- Module or path: ${input:path:controller or endpoint path}
- Desired behavior: ${input:goal:what should this endpoint do?}

Code:
${input:code:paste the code here}

Return:
1. Critical issues
2. Suggested improvements
3. Any contract or test changes needed
4. A concise recommendation