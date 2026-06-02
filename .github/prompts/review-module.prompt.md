---
agent: 'agent'
name: 'review-module'
description: 'Review a complete backend module and propose optimization axes'
---

Review the following module in the backend microservice architecture and propose the most valuable optimization axes.

Analyze the module from these perspectives:
- architecture and package boundaries
- cohesion and responsibility distribution
- coupling between classes and layers
- code duplication
- maintainability
- testability
- performance risks
- transaction boundaries
- persistence design
- API design
- error handling
- observability
- security implications
- naming and readability
- opportunities for simplification

Context:
- Module name: ${input:module:module or package name}
- Business purpose: ${input:goal:what this module is supposed to do}
- Constraints: ${input:constraints:technical or product constraints}

Codebase excerpt:
${input:code:paste the module files or the most relevant parts here}

Return:
1. Executive summary of the module quality
2. Top optimization axes ordered by impact
3. Concrete issues found in the current design
4. Refactoring opportunities with the best cost / value ratio
5. Risks if the current design is kept as-is
6. Suggested next iteration plan