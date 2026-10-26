# Engineering Decisions

This document explains selected engineering decisions in JogatinaStore, including the problems they address, their trade-offs, and their limitations.

## 1. Conditional Stock Updates

**Problem**

Stock availability can change between checking the available quantity and updating the inventory. A separate read followed by an unconditional update can therefore allow an operation to deduct more stock than is available.

**Decision**

Use a conditional JPQL update that includes the available-quantity requirement in the database update itself.

**Trade-offs**

- Combines the stock condition and quantity update into a single database operation.
- Avoids relying solely on an earlier application-level availability check.
- Requires the application to inspect the affected-row count to determine whether the update succeeded.

**Limitations**

The conditional update protects the quantity invariant for that operation, provided success is determined from the affected-row count. It does not, by itself, guarantee that the entire checkout workflow is free from concurrency issues.

## 2. Transactional Checkout

**Problem**

Checkout coordinates multiple operations, including cart validation, stock operations, and order creation. A partial failure could leave the system in an inconsistent state if these operations were committed independently.

**Decision**

Coordinate checkout operations within a transaction so that participating database changes can be committed or rolled back together.

**Trade-offs**

- Helps preserve consistency across related database operations.
- Requires transaction boundaries to cover the participating operations correctly.
- Can increase transaction duration when multiple operations are involved.

**Limitations**

A database transaction does not automatically reverse external side effects or guarantee correctness if some operations execute outside the transaction. Transaction boundaries and rollback behavior must be verified against the actual implementation.

## 3. Dynamic Product Filtering

**Problem**

Product listing supports combinations of optional filters. Creating a separate repository query for every possible combination would increase duplication and maintenance overhead.

**Decision**

Use Spring Data JPA Specifications to compose predicates dynamically according to the filters supplied by the client.

**Trade-offs**

- Supports flexible combinations of filters without requiring a dedicated query for each combination.
- Keeps filtering logic composable and reusable.
- Can make query behavior harder to understand when specifications become overly complex.

**Limitations**

Specifications do not automatically guarantee efficient SQL. Generated queries and database performance still need to be evaluated as filtering requirements and data volume grow.

## 4. Separation of Responsibilities

**Problem**

Mixing HTTP handling, use-case orchestration, business rules, and persistence concerns makes code harder to maintain and test.

**Decision**

Organize business components around application services, domain models, infrastructure, and presentation concerns where applicable. API documentation is defined separately from controller implementations.

**Trade-offs**

- Makes responsibilities easier to locate and reason about.
- Supports focused testing and changes within individual components.
- Introduces additional classes and package structure compared with a simpler layered implementation.

**Limitations**

Package organization alone does not guarantee proper separation. Dependencies and business logic must continue to follow the intended boundaries as the application evolves.

## 5. Database Schema Versioning

**Problem**

Database schema changes need to be repeatable and consistent across development and test environments.

**Decision**

Use Flyway versioned SQL migrations to manage schema changes, organized by business domain.

**Trade-offs**

- Provides a traceable history of schema changes.
- Allows environments to apply migrations in a controlled sequence.
- Requires migration files to be maintained carefully because applied migrations should not be modified casually.

**Limitations**

Migration versioning does not eliminate the need to test schema changes, plan compatibility with existing data, or coordinate changes with application releases.

## 6. Automated Testing Strategy

**Problem**

Business rules and interactions between application components need different levels of verification. Unit tests alone cannot demonstrate that HTTP endpoints, persistence, and database behavior work together correctly.

**Decision**

Use JUnit and Mockito for unit testing, and Spring Boot Test, RestAssured, and Testcontainers for integration testing.

**Trade-offs**

- Unit tests support fast, focused feedback on business behavior.
- Integration tests exercise interactions across application and infrastructure boundaries.
- Container-based tests provide a more realistic database environment than isolated mocks, at the cost of additional execution time and infrastructure requirements.

**Current Scope**

Existing tests focus primarily on cart and checkout scenarios, with unit and integration tests covering selected behaviors. This should not be interpreted as comprehensive coverage of every module or business scenario.

See [Testing Strategy](testing-strategy.md) for details about test organization, fixtures, and test boundaries.

## 7. Shared Exception Handling and Logging

**Problem**

Duplicated exception-response logic and inconsistent logging can make API behavior harder to understand and failures harder to diagnose.

**Decision**

Centralize shared exception-handling functionality and use configurable application logging.

**Trade-offs**

- Promotes consistent error responses across endpoints.
- Reduces duplicated exception-handling code.
- Requires care to avoid hiding useful diagnostic details or exposing internal information to API clients.

**Limitations**

Centralized handling does not replace appropriate exception classification, meaningful log context, or tests that verify the API's error responses.