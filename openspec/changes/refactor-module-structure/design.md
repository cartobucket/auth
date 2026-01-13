# Design: Module Structure Refactoring

## Context

The Revet Auth project currently uses five Gradle modules with naming that reflects implementation details (`auth-data-postgres-client`) rather than architectural roles. This refactoring consolidates to three modules with clear responsibilities:

- **core**: Technology-agnostic domain logic
- **persistence**: Database-specific implementation
- **web**: HTTP/REST concerns and main application entry point

**Stakeholders:** Developers maintaining or extending the codebase.

**Constraints:**
- Must maintain backward compatibility for external API consumers
- Must preserve existing functionality
- Kotlin codebase must continue to work

## Goals / Non-Goals

**Goals:**
- Simplify module structure from 5 to 3 modules
- Establish clear naming conventions reflecting architectural roles
- Merge related web concerns into single application module
- Maintain clean separation between domain, persistence, and web layers

**Non-Goals:**
- Changing any external API contracts
- Refactoring internal code logic
- Adding new features
- Changing the language (entire codebase is Kotlin)

## Decisions

### Decision 1: Module Naming Convention

**Choice:** Use role-based names (`core`, `persistence`, `web`) instead of technology-based names.

**Rationale:** Role-based names are:
- Stable across technology changes (e.g., switching from PostgreSQL to another DB)
- Self-documenting for new developers
- Aligned with Clean Architecture / Hexagonal Architecture terminology

**Alternatives considered:**
- Keep current names: Rejected because `auth-data-postgres-client` leaks implementation details
- Use `domain`/`infrastructure`/`presentation`: Considered but `core`/`persistence`/`web` is more concise

### Decision 2: DTOs and Interfaces in Core

**Choice:** Place DTOs and API interfaces in `core` module; place route implementations, mappers, and validators in `web` module.

**Rationale:**
- DTOs and interfaces are contracts, not implementations
- Keeping contracts in `core` allows other modules to depend on them without pulling in web dependencies
- Follows clean architecture principles - interfaces define boundaries, implementations live in outer layers
- The `web` module contains only implementation details (routes, mappers, validators)

**Package structure in core:**
- `com.revethq.auth.core.api.dto.*` - REST API DTOs
- `com.revethq.auth.core.api.interfaces.*` - REST API interfaces
- `com.revethq.auth.core.authorization.dto.*` - OAuth/OIDC DTOs
- `com.revethq.auth.core.authorization.interfaces.*` - OAuth API interface

**Package structure in web:**
- `com.revethq.auth.web.api.routes.*` - REST route implementations
- `com.revethq.auth.web.authorization.routes.*` - OAuth route implementation
- Application configuration and resources from `auth-single-server`

**Alternatives considered:**
- Keep DTOs/interfaces in web: Rejected because they are contracts, not implementations
- Create separate `api` module for DTOs/interfaces: Rejected as over-engineering for this project size

### Decision 3: Package Naming Structure

**Choice:** Use hierarchical package names reflecting module and submodule.

```
com.revethq.auth.{module}.{layer}.*
```

**Examples:**
- `com.revethq.auth.core.domain.User`
- `com.revethq.auth.persistence.entities.User`
- `com.revethq.auth.web.api.dto.UserRequest`

**Rationale:**
- Clear mapping from package to module
- Easy to identify layer from fully-qualified class name
- Supports IDE navigation and refactoring tools

### Decision 4: File Movement Strategy

**Choice:** Move files in bulk per module, updating imports afterward.

**Steps:**
1. Create new module directory structure
2. Copy Gradle build files and update
3. Move source files to new locations
4. Update package declarations in all files
5. Fix import statements
6. Delete old module directories
7. Update settings.gradle

**Rationale:** Bulk moves are cleaner than incremental refactoring and reduce risk of intermediate broken states.

## Design Validation Scenarios

The following scenarios validate that this module structure supports anticipated use cases.

### Scenario 1: Direct Service Access

**Use case:** Include `core` + `persistence` as dependencies in another Quarkus project to call services directly in Kotlin, bypassing HTTP entirely.

**How it works:**
- Consumer depends on `com.revethq.auth:persistence`
- Gets service implementations + domain classes + service interfaces (transitive via `core`)
- Calls `UserService`, `ApplicationService`, etc. directly without HTTP overhead

**Why the design supports this:** Service interfaces in `core` are implementation-agnostic. The `persistence` module provides concrete implementations without requiring `web`.

### Scenario 2: Alternative HTTP Implementation

**Use case:** Use `core` as a dependency in a Micronaut project to implement a compatible HTTP API using shared JAX-RS interfaces and DTOs.

**How it works:**
- Consumer depends on `com.revethq.auth:core`
- Implements `AuthorizationServerApi`, `UsersApi`, etc. using Micronaut's JAX-RS support
- Reuses DTOs for request/response serialization ensuring API compatibility

**Why the design supports this:** JAX-RS interfaces and DTOs in `core` are framework-agnostic contracts. Any JAX-RS-compatible framework (Quarkus, Micronaut, Jersey) can implement them.

### Scenario 3: Selective Dependencies via Maven Central

**Use case:** Publish modules as separate artifacts so consumers choose what they need without pulling in unused code.

**How it works:**
- JReleaser publishes `core`, `persistence`, `web` as separate Maven artifacts in a single release
- Consumer depends only on what they need:
  - `core` only → contracts for alternative implementations
  - `persistence` → direct service access (includes `core`)
  - `web` → full application (includes all)

**Why the design supports this:** Each module is a separate Gradle project producing its own artifact. No fat jar required.

### Scenario 4: Alternative Persistence (LDAP)

**Use case:** Create a `persistence-ldap` module implementing `UserService`, `GroupService` backed by LDAP, while PostgreSQL handles OAuth configuration (Applications, Clients, Scopes, etc.).

**How it works:**
- `persistence-ldap` module depends on `core`, implements `UserService`, `GroupService`
- Consumer depends on both `persistence` and `persistence-ldap`
- CDI alternatives select LDAP vs PostgreSQL per service based on configuration:
  ```kotlin
  @Alternative
  @Priority(1)
  @IfBuildProperty(name = "auth.user-store", stringValue = "ldap")
  class LdapUserService : UserService { ... }
  ```

**Why the design supports this:** Service interfaces in `core` allow multiple implementations. CDI wiring selects the appropriate implementation at runtime.

**LDAP-appropriate services:** User, Profile, Group, GroupMember (identity data)
**Database-only services:** Application, Client, AuthorizationServer, Scope, Schema, Template, SigningKey, Tokens (OAuth configuration)

### Scenario 5: Alternative Authorization Endpoint (PKI)

**Use case:** Create an alternative authorization endpoint using client certificates (PKI) instead of username/password authentication.

**How it works:**
- New module or alternative implementation implements `AuthorizationServerApi` from `core`
- Extracts client certificate from TLS context
- Validates certificate chain and maps DN to user identity
- Issues authorization code using existing service layer
- Does NOT depend on `web` (replaces that implementation)

**Why the design supports this:** The `AuthorizationServerApi` interface in `core` defines the contract. Any module can provide an alternative implementation without depending on the default `web` implementation.

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| Large number of file changes | Use IDE refactoring tools; verify with build after each module |
| Import statement errors | Run compilation frequently; fix incrementally |
| Broken tests | Run full test suite before removing old modules |
| Git history fragmentation | Git tracks content, so history should be preserved for renamed files |

## Migration Plan

### Phase 1: Create New Modules (Non-Breaking)
1. Create `core/`, `persistence/`, `web/` directories
2. Add `build.gradle` files with correct dependencies
3. Update `settings.gradle` to include new modules (keep old modules temporarily)

### Phase 2: Move Core Module
1. Move all files from `auth-data/src` to `core/src`
2. Update package declarations from `com.revethq.auth.data.*` to `com.revethq.auth.core.*`
3. Verify `core` module compiles

### Phase 3: Move Persistence Module
1. Move all files from `auth-data-postgres-client/src` to `persistence/src`
2. Update package declarations from `com.revethq.auth.postgres.client.*` to `com.revethq.auth.persistence.*`
3. Update imports referencing `auth-data` to use `core`
4. Verify `persistence` module compiles

### Phase 4: Move API Contracts to Core
1. Move DTOs from `auth-api/src/.../dto/` to `core/src/.../core/api/dto/`
   - Update package from `com.revethq.auth.api.dto.*` to `com.revethq.auth.core.api.dto.*`
2. Move interfaces from `auth-api/src/.../interfaces/` to `core/src/.../core/api/interfaces/`
   - Update package from `com.revethq.auth.api.interfaces.*` to `com.revethq.auth.core.api.interfaces.*`
3. Move DTOs from `auth-authorization-server-api/src/.../dto/` to `core/src/.../core/authorization/dto/`
   - Update package from `com.revethq.auth.authorization.server.dto.*` to `com.revethq.auth.core.authorization.dto.*`
4. Move interface from `auth-authorization-server-api/src/.../interfaces/` to `core/src/.../core/authorization/interfaces/`
   - Update package from `com.revethq.auth.authorization.server.interfaces.*` to `com.revethq.auth.core.authorization.interfaces.*`
5. Verify `core` module compiles

### Phase 5: Move Route Implementations to Web
1. Move routes from `auth-api/src/.../routes/` to `web/src/.../web/api/routes/`
2. Move validators from `auth-api/src/.../validators/` to `web/src/.../web/api/validators/`
3. Move routes from `auth-authorization-server-api/src/.../routes/` to `web/src/.../web/authorization/routes/`
4. Move validators from `auth-authorization-server-api/src/.../validators/` to `web/src/.../web/authorization/validators/`
5. Move application configuration from `auth-single-server`:
   - `src/main/resources/application.properties` → `web/src/main/resources/`
   - `src/main/openapi/` → `web/src/main/openapi/`
   - Any other resources (templates, static files)
6. Update `web/build.gradle` to include Quarkus application plugins and all necessary dependencies
7. Update imports to use new `core` packages
8. Verify `web` module compiles and runs: `./gradlew :web:quarkusDev`

### Phase 6: Cleanup
1. Remove old module directories (`auth-data`, `auth-data-postgres-client`, `auth-api`, `auth-authorization-server-api`, `auth-single-server`)
2. Update `settings.gradle` to remove old module references
3. Run full test suite
4. Verify application starts and functions correctly

### Rollback Plan
If issues arise:
1. Revert all changes via git
2. Old modules remain functional until explicitly deleted

## Open Questions

None - all modules use Kotlin consistently.
