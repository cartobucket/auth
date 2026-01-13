# Project Context

## Purpose

Revet Auth is an opinionated OAuth 2.1 and OpenID Connect (OIDC) implementation designed as an educational and practical platform for developers to understand and explore OAuth/OIDC concepts. This project is **NOT production-ready** and serves as both a learning tool and a functional authorization server environment.

**Key capabilities:**
- OAuth 2.1 Authorization Code Flow (for clients/users)
- Client Credentials Flow (for applications/system-to-system)
- OpenID Connect (OIDC) protocol support
- Refresh token support
- Multiple authorization server instances
- User profile schema validation
- HTML template customization for login screens

## Tech Stack

**Languages:**
- Java 21 (primary)
- Kotlin 2.2.10 (domain models)

**Frameworks & Libraries:**
- Quarkus 3.8.4 (application framework)
- RESTEasy Reactive (JAX-RS REST endpoints)
- Hibernate ORM with Panache (data persistence)
- SmallRye JWT Build (token generation)
- jose4j (JWT validation)
- Qute (HTML templating)
- Jakarta Validation / Hibernate Validator

**Build & Tooling:**
- Gradle 8.x with Groovy DSL
- Spotless 6.16.0 (code formatting)
- OpenAPI Generator 7.2.0

**Database:**
- PostgreSQL

**Testing:**
- JUnit 5 (Quarkus testing support)

## Project Conventions

### Code Style

**Formatting:**
- Google Java Format (1.16.0) with AOSP dialect
- Enforced via Spotless plugin
- Automatic formatting on changed files (ratchets from `origin/main`)

**Naming Conventions:**
- Package structure: `com.revethq.auth.<module>.<layer>`
- Classes: PascalCase (e.g., `ApplicationService`, `UserRepository`)
- Constants: UPPER_SNAKE_CASE
- Methods: camelCase

**Language Split:**
- Kotlin: Domain models, service interfaces, DTOs, persistence layer, and REST routes

### Architecture Patterns

**Multi-Module Gradle Structure:**
```
web           # Main executable Quarkus app - REST routes, validators, application config
├── persistence   # PostgreSQL persistence - JPA entities, repositories, service implementations
└── core          # Core domain - domain models, service interfaces, DTOs, API interfaces
```

**Module Responsibilities:**
- **core**: Technology-agnostic domain classes, service interfaces, DTOs, and JAX-RS API interfaces
- **persistence**: Database-specific implementation with JPA entities, Panache repositories, and service implementations
- **web**: HTTP/REST concerns including route implementations, validators, and the main application entry point

**Layered Architecture:**
```
REST Controllers (interfaces)
       ↓
Services (business logic)
       ↓
Repositories (Panache DAOs)
       ↓
JPA Entities (persistence)
```

**Key Patterns:**
- **Mapper Pattern**: Domain models separate from JPA entities, with mappers converting between layers
- **Repository Pattern**: Quarkus Panache DAO pattern with HQL/JPQL queries
- **Service Layer**: Abstract interfaces in `core`, concrete implementations in `persistence`
- **Dependency Injection**: Quarkus Arc (CDI)

### Testing Strategy

- Unit tests with JUnit 5 and Quarkus test support
- Integration test examples in `examples/oidc_test/` (Python/Django OAuth client)
- REST API examples in `examples/rest_collection/` (Bruno/Postman collections)
- Manual testing via Swagger UI at `/q/swagger-ui/`

### Git Workflow

- Main branch: `main`
- Commit messages: lowercase imperative with PR references (e.g., "add refresh token support (#33)")
- PRs numbered and referenced in commits

## Domain Context

**Core Domain Model:**

| Entity | Description |
|--------|-------------|
| AuthorizationServer | Top-level container for all authorization concepts |
| Application | System-to-system actor (client credentials flow) |
| ApplicationSecret | Credentials for applications |
| Client | User-facing application actor (authorization code flow) |
| User | Authentication subject |
| Profile | User profile data with schema validation |
| SigningKey | JWT signing keys per authorization server |
| Scope | OAuth scopes for access control |
| Template | HTML templates for login/consent screens (Base64 encoded) |
| Schema | JSON schemas for profile validation |
| RefreshToken | Refresh tokens for token renewal |
| IdentityProvider | Federated authentication configuration |

**OAuth/OIDC Flows:**
1. **Authorization Code Flow**: User authentication via browser redirect, returns authorization code exchanged for tokens
2. **Client Credentials Flow**: Direct token request for server-to-server communication
3. **Token Refresh**: Use refresh tokens to obtain new access tokens

## Important Constraints

- **Not Production Ready**: This is an educational project; do not use in production environments
- **PostgreSQL Required**: The application requires a PostgreSQL database
- **Java 21**: Minimum JDK version requirement
- **Single Deployment Model**: The `web` module bundles all components as the main application

## External Dependencies

**Required Services:**
- PostgreSQL database (localhost:5432 by default)

**Environment Variables:**
```
CB_AUTH_DATABASE_USERNAME   # Default: auth/revet-auth
CB_AUTH_DATABASE_PASSWORD   # Default: auth/notsecure
CB_AUTH_DATABASE_JDBC_URL   # Default: jdbc:postgresql://localhost:5432/auth
CB_AUTH_LOG_SQL             # Default: false
```

**Runtime Ports:**
- HTTP: 5000 (configurable)

**Docker Compose:**
- `docker-compose.yaml` provides PostgreSQL with health checks and volume persistence
