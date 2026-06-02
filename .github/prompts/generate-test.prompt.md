---
agent: 'agent'
name: 'generate-test'
description: 'Generate focused backend tests'
---

Generate tests for the following backend code.

Prefer:
- unit tests for business rules
- slice tests for controllers or repositories when appropriate
- integration tests only when the behavior depends on the full stack or database-specific behavior

Context:
- Test target: ${input:path:class or method being tested}
- Desired coverage: ${input:goal:what behavior should be verified?}

Code:
${input:code:paste the code here}

Return:
1. Test cases to add
2. A recommended test type for each case
3. The test code
4. Any fixtures or setup needed