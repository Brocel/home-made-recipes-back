# Design patterns instructions

## Scope

Apply these rules when Copilot is deciding whether to introduce, preserve, or refactor a design pattern in this backend
repository.

This file is about architecture and design decisions, not about task-specific prompts or reusable skill workflows.

## Core rule

Design patterns are tools, not goals.

Do not introduce a design pattern unless it solves a real problem in the current code.

Prefer the simplest readable solution that satisfies the requirement.

## Decision rule

Before introducing any pattern, evaluate:

- Is the current code actually hard to understand, extend, or test?
- Is there a repeated variation that needs a stable abstraction?
- Is there more than one valid implementation today or in the near future?
- Does the repository already use this pattern in the same area?
- Would a simpler refactor solve the problem first?
- Does the pattern remove duplication, or only make the code look more “architectural”?

If the answer to these questions is unclear, do not introduce the pattern.

## Strong preference order

When multiple solutions are possible, prefer this order:

1. Small refactor inside the current class or module
2. Extract method or private helper
3. Simple interface or mapper
4. Pattern with clear business value
5. Pattern with inheritance or extra abstraction

Do not jump directly to a pattern when a smaller change is sufficient.

## Patterns to use only when justified

### Strategy

Use when:

- the same workflow has multiple interchangeable behaviors
- business rules vary by type, mode, or context
- conditional branching is growing and becoming hard to maintain

Avoid when:

- there is only one implementation
- the branching is small and easy to read
- the variation can be handled by a simple method or enum

### Factory

Use when:

- object creation is complex
- creation depends on runtime input
- multiple variants must be created consistently

Avoid when:

- the factory would only wrap a constructor
- the object is simple and obvious to instantiate
- the factory adds indirection without value

### Builder

Use when:

- construction has many optional parameters
- the object is hard to read when created directly
- step-by-step construction improves clarity

Avoid when:

- a constructor or record is enough
- the builder only hides a simple object creation
- the builder is used for small DTOs or trivial values

### Adapter

Use when:

- integrating an external system
- translating between incompatible models or APIs
- isolating vendor-specific code from the rest of the application

Avoid when:

- the mapping is trivial and local
- the adapter would duplicate a simple mapper

### Facade

Use when:

- a subsystem is complex and should be accessed through one simpler entry point
- multiple low-level services must be coordinated from one stable API

Avoid when:

- the facade would become a “god service”
- the underlying flow is already simple

### Specification

Use when:

- rules must be combined dynamically
- filtering logic needs to be composable
- business predicates need to be reusable and readable

Prefer Querydsl predicates for persistence filtering.

Avoid when:

- the rule is only used once
- a simple conditional is easier to read
- the abstraction hides the real query behavior

### Template Method

Use when:

- multiple implementations share the same workflow
- only a few steps vary

Avoid when:

- inheritance is being forced
- the workflow is not stable
- composition would be clearer

### Decorator

Use when:

- you want to extend behavior without changing the wrapped component
- cross-cutting behavior must be layered cleanly

Prefer Spring bean composition when it keeps the code simpler.

Avoid when:

- the wrapping chain becomes difficult to follow
- the extension can be handled by a simple service call

### Observer / Domain Events

Use when:

- a domain event represents a meaningful business fact
- multiple side effects must react to the same action
- the producer should not know the consumers

Avoid when:

- the event is only being used to avoid direct calls for no reason
- the flow becomes harder to understand than a direct service call

Prefer explicit service orchestration when the operation is synchronous and local.

## Spring-specific guidance

Prefer these Spring-friendly patterns when they fit the problem:

- Dependency Injection
- Configuration Properties
- Bean composition
- Mapper interfaces with MapStruct
- Domain events only when there is a real decoupling need
- Service orchestration in the application layer

Avoid:

- Service Locator
- Static access to services
- Hidden global state
- Deep inheritance trees
- Abstract base classes created only for reuse
- Generic “framework” layers that do not add value
- Pattern-heavy code that makes the flow harder to follow

## Domain modeling guidance

Prefer:

- Value Objects for small immutable concepts
- Aggregates when consistency boundaries matter
- Domain services when a rule does not belong to one entity
- Domain events when a business event needs to be observed
- Simple composition before inheritance

Do not force DDD or GoF patterns into simple CRUD features.

## Backend-specific guidance

For this project, prefer patterns that improve clarity in:

- REST API orchestration
- validation and error handling
- persistence query composition
- mapping between DTOs and entities
- external integrations
- authentication and authorization flows

Be especially conservative with patterns in:

- controllers
- DTOs
- repositories
- Flyway migrations
- utility code

## What to avoid

Do not introduce:

- Singleton classes
- God objects
- Overly generic base classes
- Abstract factories without a real family of products
- Builders for simple records or DTOs
- Factories that only wrap constructors
- Strategy interfaces with one implementation
- Event-driven flows where a direct service call is clearer
- Design patterns used only to look “clean” or “enterprise”

## Review checklist

When proposing or reviewing a pattern, verify:

- the problem is real and present
- the pattern makes the code easier to understand
- the pattern reduces duplication or branching
- the pattern matches the rest of the repository
- the pattern does not hide simple logic
- the pattern does not add unnecessary layers
- the code remains easy to test

## Output expectations

When Copilot suggests or generates a design pattern:

- explain why the pattern is needed
- explain why simpler alternatives are insufficient
- keep the implementation minimal
- keep class and package names explicit
- do not add extra abstractions unless they solve a specific problem
- prefer clarity over elegance