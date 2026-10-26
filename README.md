# JogatinaStore

A modular monolith e-commerce application built with Java and Spring Boot, focused on backend development, domain modeling, data integrity, automated testing, and maintainable software design.

The project simulates core e-commerce workflows, including user management, product catalog, inventory, shopping carts, checkout, and order management. It is also a practical environment for exploring engineering trade-offs in a realistic application.

> **Project status:** Under active development. Features are identified as implemented, in progress, or planned to distinguish the current state from the intended scope.

## 1. Project Overview

JogatinaStore is a REST API for an online store, designed as a modular monolith. Its codebase is organized around business capabilities, with modules responsible for specific parts of the application.

The project emphasizes:

- Separation of responsibilities between application, domain, infrastructure, and presentation concerns where applicable.
- Business rules and data integrity in critical workflows.
- Transaction management across related operations.
- Automated tests at different levels.
- Dynamic product filtering and paginated API responses.
- API documentation through OpenAPI and Swagger UI.

The goal is to build a portfolio project that demonstrates not only the ability to implement endpoints, but also the ability to reason about application structure, persistence, consistency, and testing.

## 2. Features

Feature status reflects the current implementation and is subject to change as development progresses.

### Identity and Access Management — `iam`

- [x] User management.
- [x] Authentication and authorization infrastructure.
- [x] Role-based access control.
- [ ] Further refinement of role management and access policies.

### Product Catalog — `catalog`

- [x] Product management.
- [x] Category management.
- [x] Brand management.
- [x] Dynamic product filtering using Spring Data JPA Specifications.
- [ ] Further catalog capabilities and refinements.

### Customer — `customer`

- [x] Customer module and its application structure.
- [ ] Complete and expand customer-facing use cases.

### Inventory — `inventory`

- [x] Stock management foundation.
- [x] Conditional stock updates to protect available quantities.
- [ ] Further inventory workflows and edge-case coverage.

### Sales — `sales`

- [x] Shopping cart functionality.
- [x] Checkout workflow foundation.
- [x] Order creation as part of checkout.
- [x] Transactional coordination of checkout operations.
- [ ] Expanded order lifecycle and sales scenarios.

### Supporting capabilities

- [x] Centralized exception handling.
- [x] Configurable application logging.
- [x] Database migrations with Flyway.
- [x] API documentation with OpenAPI/Swagger.
- [x] Automated tests using JUnit, Mockito, RestAssured, and Testcontainers.
- [x] Continuous integration workflow for running tests.
- [ ] Additional observability capabilities, such as metrics and distributed tracing, where not yet implemented.
- [ ] Payment processing.

## 3. Architecture

JogatinaStore follows the **modular monolith** architectural style. The application is deployed as a single unit, while its code is divided into modules organized around business capabilities.

Each module owns a specific area of the business. Within modules, packages separate concerns such as application services, domain models, persistence, HTTP controllers, and API documentation where appropriate.

This structure aims to keep related functionality together, make responsibilities easier to understand, and reduce unnecessary coupling without introducing the operational complexity of microservices.

### High-level structure

```text
JogatinaStore
├── iam
│   ├── user
│   ├── authentication
│   └── role
├── catalog
│   ├── brand
│   ├── category
│   └── product
├── customer
│   └── customer
├── inventory
│   └── stock
├── sales
│   ├── cart
│   ├── checkout
│   └── order
├── config
│   ├── openapi
│   └── security
├── observability
│   ├── logging
│   ├── metrics
│   └── tracing
└── shared
    ├── exception
    ├── pagination
    └── utils
```

The presence of a package in this structure does not necessarily mean every capability beneath it is complete.

For a deeper explanation of module responsibilities, internal organization, and dependencies, see [Architecture](docs/architecture.md).

## 4. Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Primary programming language |
| Spring Boot 3.4.0 | Application framework |
| Spring Web | REST API development |
| Spring Data JPA | Persistence and repository abstraction |
| Hibernate | ORM implementation |
| MySQL | Relational database |
| Flyway | Versioned database migrations |
| Spring Security | Authentication and authorization |
| OpenAPI / Swagger UI | API documentation |
| JUnit | Automated testing |
| Mockito | Test doubles and isolated unit tests |
| Spring Boot Test | Spring application testing |
| RestAssured | HTTP API testing |
| Testcontainers | Tests using containerized dependencies |
| GitHub Actions | Continuous integration |

The table describes the project's intended or established technology stack; individual integrations and their current coverage may vary as implementation progresses.

## 5. Engineering Practices

The project explores practical engineering techniques through concrete implementation choices.

- **Modular organization:** groups code around business capabilities instead of relying exclusively on technical layers across the entire application.
- **Transactional checkout:** coordinates related checkout operations within a database transaction to prevent partial completion when a transactional operation fails.
- **Conditional stock updates:** applies stock changes with an availability condition in the database update, helping protect stock quantity invariants when the update result is validated.
- **Dynamic filtering:** uses Spring Data JPA Specifications to compose product search criteria without requiring a separate repository method for every filter combination.
- **Automated testing:** combines unit tests with integration tests that exercise application and persistence behavior.
- **Reusable test data:** uses fixtures and Object Mother patterns to make test setup more consistent and readable.
- **Versioned schema changes:** uses Flyway migrations to track database evolution.
- **API contract documentation:** separates endpoint documentation from controller implementation through dedicated documentation interfaces where applicable.

These practices involve trade-offs rather than being universal solutions. Their motivations, alternatives, limitations, and implementation details are documented in [Engineering Decisions](docs/engineering-decisions.md).

## 6. API Documentation

The REST API is documented using OpenAPI, with Swagger UI providing an interactive interface for inspecting endpoints and trying requests.

When the application is running, the documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI specification is typically available at:

```text
http://localhost:8080/v3/api-docs
```

These URLs assume the default port and standard Springdoc configuration. Actual availability depends on the active application configuration.

The API covers the project's identity, catalog, customer, inventory, and sales capabilities according to their implementation status.

For a quick, module-by-module endpoint index, see [API Reference](docs/api-reference.md).

## 7. Getting Started

### Prerequisites

- JDK 17.
- Maven, or the project's Maven Wrapper if included in the repository.
- MySQL.
- Git.

### Clone the repository

```bash
git clone <repository-url>
cd jogatinastore
```

Replace `<repository-url>` with the repository's actual Git URL.

### Configure the database

Create a MySQL database and configure the application's database connection using the project's supported configuration properties or environment variables.

For example, Spring Boot commonly uses the following properties:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

These are illustrative configuration entries, not a guarantee that the project currently defines these exact environment variable names. Match them to the actual application configuration before running the project.

Do not commit real credentials or secrets to version control.

### Run the application

If the Maven Wrapper is available:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatively, with Maven installed:

```bash
mvn spring-boot:run
```

Ensure the database is available and the connection settings are correct before starting the application.

For the exact environment variables, profiles, database setup, and development commands, see [Development Guide](docs/development-guide.md).

## 8. Testing

The project uses automated tests to verify business behavior, application integration, and persistence-related scenarios.

### Testing tools

- **JUnit:** test organization and assertions.
- **Mockito:** isolated tests using mocks and other test doubles.
- **Spring Boot Test:** integration with the Spring application context.
- **RestAssured:** HTTP request and response validation.
- **Testcontainers:** integration tests against containerized dependencies.
- **Fixtures and Object Mother:** reusable test data setup.

### Test organization

Tests are organized into separate areas for unit tests and integration tests. Integration tests also contain supporting configuration, fixtures, infrastructure setup, and test support utilities.

Current test scenarios focus particularly on shopping cart and checkout behavior. The scope should not be interpreted as complete coverage of every module or business rule.

### Run the tests

Using the Maven Wrapper:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

Or, with Maven installed:

```bash
mvn test
```

Some integration tests may require Docker to be available if they use Testcontainers.

For test boundaries, conventions, data setup, and known coverage gaps, see [Testing Strategy](docs/testing-strategy.md).

## 9. Project Structure

The repository separates business modules, shared support code, application configuration, database migrations, and automated tests.

```text
jogatinastore/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── .../
│   │   │       ├── iam/
│   │   │       ├── catalog/
│   │   │       ├── customer/
│   │   │       ├── inventory/
│   │   │       ├── sales/
│   │   │       ├── config/
│   │   │       ├── observability/
│   │   │       └── shared/
│   │   └── resources/
│   │       └── db/
│   │           └── migration/
│   └── test/
│       └── java/
│           └── .../
│               ├── unittests/
│               └── integrationtests/
│                   ├── config/
│                   ├── fixture/
│                   ├── infrastructure/
│                   ├── support/
│                   └── tests/
├── docs/
├── pom.xml
└── README.md
```

### Module internals

Business modules use internal packages to organize their responsibilities. For example, the catalog's product component follows this structure:

```text
product/
├── application/
│   ├── dto/
│   ├── mapper/
│   ├── service/
│   └── snapshot/
├── domain/
│   ├── exception/
│   └── model/
├── infrastructure/
│   ├── persistence/
│   └── specification/
└── presentation/
    ├── docs/
    ├── filter/
    └── http/
```

The exact internal organization varies by component according to its needs; not every component necessarily contains every package shown above.

Database migrations are maintained under `src/main/resources/db/migration`. Test code is separated into unit and integration test areas to distinguish isolated behavior verification from tests involving application components and external dependencies.

For the full architectural explanation and the responsibilities of each module, see [Architecture](docs/architecture.md).

## 10. Detailed Documentation

The documentation below provides deeper explanations of the project's architecture, implementation choices, testing approach, and operational concerns.

| Document | Description |
|---|---|
| [Architecture](docs/architecture.md) | Modular monolith structure, module responsibilities, internal organization, and dependencies. |
| [API Reference](docs/api-reference.md) | Endpoints grouped by module, HTTP methods, parameters, permissions, and expected responses. |
| [Engineering Decisions](docs/engineering-decisions.md) | Selected engineering decisions, their motivations, alternatives, trade-offs, and limitations. |
| [Testing Strategy](docs/testing-strategy.md) | Test types, tools, test data setup, integration testing approach, and known coverage gaps. |
| [Data Modeling and Persistence](docs/data-modeling-and-persistence.md) | Data model, entity relationships, persistence strategy, database constraints, queries, and migrations. |
| [Security](docs/security.md) | Authentication, authorization, JWT implementation, role-based access control, and endpoint protection. |
| [Error Handling](docs/error-handling.md) | Exception structure, centralized handling, HTTP status mapping, and error response format. |
| [Observability](docs/observability.md) | Application logging and the status of metrics, tracing, and other observability capabilities. |
| [Development Guide](docs/development-guide.md) | Local setup, configuration, database preparation, application execution, testing, and development workflow. |

This documentation is intended to explain not only **how the application is organized and used**, but also **why specific implementation choices were made**, what trade-offs they introduce, and where the current implementation has limitations.

Documentation is maintained alongside development and may identify capabilities as *Implemented*, *In Progress*, or *Planned* to distinguish the current state from future work.

## 11. Roadmap

The roadmap tracks the project's evolution toward a more complete e-commerce backend. Items are grouped by implementation status to distinguish existing capabilities from ongoing work and future improvements.

### In Progress

- [ ] Define and implement user blocking and unblocking rules, considering their impact on customer-related operations.
- [ ] Expand automated test coverage for catalog, inventory, customer, IAM, and JWT token generation and validation.
- [ ] Implement email delivery for system notifications.
- [ ] Implement product image management, including upload, storage, and retrieval.
- [ ] Extend order lifecycle management.
- [ ] Expand automated tests for business rules, edge cases, and failure scenarios.
- [ ] Complete the project's technical documentation.

### Planned

- [ ] Implement payment processing and integrate it into the checkout workflow.
- [ ] Improve observability with application metrics and distributed tracing.
- [ ] Expand CI/CD automation and prepare a deployment workflow.
- [ ] Evaluate containerized application deployment and cloud hosting.
- [ ] Review performance, database queries, and data integrity as the application grows.

### Long-Term Goals

- [ ] Evaluate asynchronous processing and messaging where business requirements justify them.
- [ ] Add operational monitoring and health visibility.
- [ ] Improve resilience and concurrency handling in critical workflows.
- [ ] Document and validate architectural decisions as the system evolves.

The roadmap is subject to change as implementation progresses. Priorities will be guided by business requirements, technical findings, and the value of each improvement rather than by adopting technologies without a clear purpose.