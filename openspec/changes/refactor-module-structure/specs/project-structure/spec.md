# Project Structure

## ADDED Requirements

### Requirement: Three-Module Architecture

The project SHALL be organized into exactly three modules:

1. **core** - Domain logic and API contracts (domain classes, service interfaces, exceptions, DTOs, API interfaces)
2. **persistence** - Database-specific implementation (entities, repositories, service implementations)
3. **web** - Route implementations and application entry point (routes, mappers, validators, application configuration)

#### Scenario: Module dependency direction

- **WHEN** a developer examines the module dependencies
- **THEN** the dependency graph SHALL flow as: `web` → `persistence`, `core`; `persistence` → `core`
- **AND** `core` SHALL have no dependencies on other project modules

#### Scenario: Core module contents

- **WHEN** a developer examines the `core` module
- **THEN** it SHALL contain domain classes under `com.revethq.auth.core.domain.*`
- **AND** service interfaces under `com.revethq.auth.core.services.*`
- **AND** exception classes under `com.revethq.auth.core.exceptions.*`
- **AND** REST API DTOs under `com.revethq.auth.core.api.dto.*`
- **AND** REST API interfaces under `com.revethq.auth.core.api.interfaces.*`
- **AND** OAuth/OIDC DTOs under `com.revethq.auth.core.authorization.dto.*`
- **AND** OAuth API interface under `com.revethq.auth.core.authorization.interfaces.*`
- **AND** it SHALL NOT contain any database-specific code or route implementations

#### Scenario: Persistence module contents

- **WHEN** a developer examines the `persistence` module
- **THEN** it SHALL contain JPA entities under `com.revethq.auth.persistence.entities.*`
- **AND** entity mappers under `com.revethq.auth.persistence.entities.mappers.*`
- **AND** Panache repositories under `com.revethq.auth.persistence.repositories.*`
- **AND** service implementations under `com.revethq.auth.persistence.services.*`

#### Scenario: Web module contents

- **WHEN** a developer examines the `web` module
- **THEN** it SHALL contain route implementations for two distinct APIs:
  - REST API routes under `com.revethq.auth.web.api.routes.*`
  - OAuth/OIDC routes under `com.revethq.auth.web.authorization.routes.*`
- **AND** it SHALL contain application configuration and resources
- **AND** it SHALL NOT contain DTOs or API interfaces (those belong in core)

#### Scenario: Web module is the application entry point

- **WHEN** a developer runs the application
- **THEN** the `web` module SHALL be the runnable Quarkus application
- **AND** no separate assembler or server module SHALL exist

### Requirement: API Contracts in Core

DTOs and API interfaces SHALL reside in the `core` module to establish clear contract boundaries.

#### Scenario: REST API contracts location

- **WHEN** a developer needs to modify REST API DTOs or interface definitions
- **THEN** the code SHALL be located under `com.revethq.auth.core.api.*`

#### Scenario: OAuth/OIDC contracts location

- **WHEN** a developer needs to modify OAuth 2.0 or OpenID Connect DTOs or interface definitions
- **THEN** the code SHALL be located under `com.revethq.auth.core.authorization.*`

### Requirement: Route Implementations in Web

Route implementations, mappers, and validators SHALL reside in the `web` module.

#### Scenario: REST API routes location

- **WHEN** a developer needs to modify REST management endpoint implementations
- **THEN** the code SHALL be located under `com.revethq.auth.web.api.*`

#### Scenario: OAuth/OIDC routes location

- **WHEN** a developer needs to modify OAuth 2.0 or OpenID Connect endpoint implementations
- **THEN** the code SHALL be located under `com.revethq.auth.web.authorization.*`
