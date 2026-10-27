# Engineering Decisions

This document explains selected engineering decisions in SpringEcommerce, including the problems they address, their trade-offs, and their limitations.

## 1. Conditional Stock Updates

**Problem**

Stock reservations must not reduce the available quantity below zero. Because multiple checkout requests can attempt to reserve the same stock concurrently, a quantity check performed earlier in the application may become outdated before the reservation is executed.

**Decision**

Use a conditional JPQL update to decrease the available quantity and increase the reserved quantity only when sufficient stock is available.

**Rationale**

- Enforces the stock availability condition at the database operation that performs the reservation.
- Updates available and reserved quantities atomically, avoiding an intermediate state in which only one quantity has been changed.
- Avoids loading and tracking the stock entity for a quantity operation that can be expressed directly as a conditional update.
- Uses the affected-row count to determine whether the reservation succeeded.

**Limitations**

This protects the stock quantity invariant during the reservation operation. It does not, by itself, guarantee concurrency safety for every operation in the checkout workflow.

## 2. Transactional Checkout

**Problem**

Checkout coordinates multiple operations, including cart validation, stock operations, and order creation. A partial failure could leave the system in an inconsistent state if these operations were committed independently.

**Decision**

Coordinate checkout operations within a transaction so that participating database changes can be committed or rolled back together.

**Trade-offs**

- **Improved atomicity vs. longer lock duration:** Grouping checkout operations in one transaction prevents participating database changes from being committed independently. However, locks acquired during the transaction may be held until commit or rollback, increasing contention when concurrent operations access the same records.

**Limitations**

A database transaction does not automatically reverse external side effects or guarantee correctness if some operations execute outside the transaction. Transaction boundaries and rollback behavior must be verified against the actual implementation.

## 3. Dynamic Product Filtering

**Problem**

Product listing supports combinations of optional filters. Creating a separate repository query for every possible combination would increase duplication and maintenance overhead.

**Decision**

Use Spring Data JPA Specifications to compose predicates dynamically according to the filters supplied by the client.

**Trade-offs**

- **Flexibility vs. query complexity:** Specifications support combinations of optional filters without requiring a separate query for each combination. However, as filtering rules grow, the resulting predicates and generated SQL can become harder to understand, test, and optimize.

**Limitations**

Specifications do not automatically guarantee efficient SQL. Generated queries and database performance still need to be evaluated as filtering requirements and data volume grow.

## 4. Separation of Responsibilities

**Problem**

Mixing HTTP handling, use-case orchestration, business rules, and persistence concerns makes code harder to maintain and test.

**Decision**

Organize business components around application services, domain models, infrastructure, and presentation concerns where applicable. API documentation is defined separately from controller implementations.

**Trade-offs**

- **Separation of concerns vs. structural complexity:** Separating responsibilities makes components easier to understand, test, and change independently. However, it introduces additional classes, interfaces, and package structure compared with a simpler implementation, increasing the amount of code developers must navigate and maintain.

**Limitations**

Package organization alone does not guarantee proper separation. Dependencies and business logic must continue to follow the intended boundaries as the application evolves.

## 5. Database Schema Versioning

**Problem**

Database schema changes need to be repeatable and consistent across development and test environments.

**Decision**

Use Flyway versioned SQL migrations to manage schema changes, organized by business domain.

**Trade-offs**

- **Traceability and controlled evolution vs. migration maintenance:** Versioned migrations provide a consistent, auditable history of schema changes. However, once a migration has been applied to a shared environment, correcting it generally requires a new migration rather than editing the original file, increasing the maintenance effort when schema changes need correction.

**Limitations**

Migration versioning does not eliminate the need to test schema changes, plan compatibility with existing data, or coordinate changes with application releases.

## 6. Automated Testing Strategy

**Problem**

Business rules and interactions between application components need different levels of verification. Unit tests alone cannot demonstrate that HTTP endpoints, persistence, and database behavior work together correctly.

**Decision**

Use JUnit and Mockito for unit testing, and Spring Boot Test, RestAssured, and Testcontainers for integration testing.

**Rationale**

- Unit tests provide fast, focused feedback on business behavior.
- Integration tests verify interactions across application and infrastructure boundaries.
- Testcontainers allows integration tests to run against a real database engine instead of relying exclusively on mocks.

**Trade-offs**

- **Realistic integration testing vs. execution cost:** Running tests against containerized infrastructure provides greater confidence in database interactions, but increases test execution time and requires a compatible container runtime.

**Current Scope**

Existing tests focus primarily on cart and checkout scenarios, with unit and integration tests covering selected behaviors. This should not be interpreted as comprehensive coverage of every module or business scenario.

See [Testing Strategy](testing-strategy.md) for details about test organization, fixtures, and test boundaries.

## 7. Shared Exception Handling and Logging

**Problem**

Duplicated exception-response logic and inconsistent logging can make API behavior harder to understand and failures harder to diagnose.

**Decision**

Centralize shared exception-handling functionality and use configurable application logging.

**Rationale**

- Centralizes exception-to-response mapping, promoting consistent API error responses.
- Reduces duplicated exception-handling code and makes shared behavior easier to maintain.

**Limitations**

Centralization does not guarantee that exceptions are classified correctly or that logs contain sufficient diagnostic context. Error responses and logging behavior must be tested to ensure clients receive appropriate information without exposing internal details.