# Module and package boundaries

## Module responsibilities

- `api`: REST controllers, and API-facing mapping.
- `app`: application bootstrap, configuration, global exception handling, and resources such as migrations and OpenAPI
  files.
- `common`: shared technical code such as utilities, exceptions, mappers, models, predicates, and validators.
- `persistence`: JPA entities, repositories, and persistence enums.
- `service`: business logic and use-case orchestration.

## Boundary rules

- Keep dependencies flowing from outer layers toward inner layers.
- Do not let API types leak into persistence.
- Do not let persistence entities leak into the API layer.
- Do not put business logic into `common` unless it is truly technical and reusable.
- If a concept belongs to one service, keep it in that service.
- Do not create shared database access across microservices.
- Do not share domain models across services.
- If a change spans multiple modules, identify the owning module first and keep the diff isolated.

## Naming and organization

- Keep packages cohesive and purpose-driven.
- Group code by responsibility, not by incidental technical detail.
- Use small, explicit mappers when crossing boundaries between layers.
- Prefer local reuse inside a service over broad shared abstractions.