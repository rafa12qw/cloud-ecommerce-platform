# Cloud E-Commerce Platform

Production-oriented cloud-native e-commerce backend built as a modular monolith
with Java 21 and Spring Boot.

## Current status

Milestone 0 establishes the application foundation:

- Maven Wrapper and repository hygiene
- Spring Web, Spring Data JPA, Bean Validation, Actuator, PostgreSQL, and Flyway
- `catalog` module package boundaries
- Development and test profiles
- Actuator health endpoint
- Baseline Spring context test

Business features are not implemented yet.

## Prerequisites

- JDK 21
- PostgreSQL 14 or newer for the development profile
- Docker for PostgreSQL integration tests

The Maven Wrapper downloads Maven automatically. A system Maven installation is
not required.

## Local setup

Create a PostgreSQL database and export the development connection settings:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/ecommerce
export DB_USERNAME=ecommerce
export DB_PASSWORD=change-me
```

The `dev` profile is active by default. Override it with
`SPRING_PROFILES_ACTIVE` when necessary.

## Run and test

Run the test suite:

```bash
./mvnw test
```

The repository and full-context integration tests start PostgreSQL 16
automatically with Testcontainers, so Docker must be running. To run the
non-Docker unit and HTTP tests only:

```bash
./mvnw -Dtest='ProductControllerTest,ProductServiceTest' test
```

Start the application:

```bash
./mvnw spring-boot:run
```

The health endpoint is available at
`http://localhost:8080/actuator/health`.

## Package structure

```text
com.rafael.ecommerce
└── catalog
    ├── api             # HTTP adapters
    ├── application     # Use cases and orchestration
    ├── domain          # Domain model and business rules
    └── infrastructure  # Persistence and external adapters
```

## Planned next step

Implement the Product Catalog vertical slice with migrations, persistence,
validated DTO-based endpoints, exception handling, and unit/integration tests.
