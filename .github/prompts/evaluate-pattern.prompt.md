---
agent: 'agent'
name: 'evaluate-pattern'
description: 'Evaluate whether a design pattern or abstraction is justified'
---

Analyze the following backend code and determine whether a design pattern, abstraction, or refactor should be applied.

Focus on:
- whether the current design is simple enough already
- whether duplication, branching, or coupling justify a pattern
- whether the repository already uses an equivalent pattern
- whether the proposed pattern would improve clarity, testability, or extensibility
- whether a smaller refactor would solve the problem better

Context:
- Scope: ${input:path:service class, group of classes, or package}
- Goal: ${input:goal:what problem needs to be solved?}
- Constraints: ${input:constraints:any known limitations or architectural rules}

Code:
${input:code:paste the relevant classes here}

Return:
1. Should a pattern be introduced: yes / no / maybe
2. The exact problem being solved
3. The simplest alternative solution
4. If a pattern is justified, which pattern fits best and why
5. Risks of over-engineering or unnecessary abstraction
6. Concrete recommendation for the codebase