# Change: Convert Remaining Java Files to Kotlin

## Why

The codebase currently has a mixed Java/Kotlin architecture with 184 Java files remaining across 4 modules while the `auth-data` module has already been converted to Kotlin (52 files). A unified Kotlin codebase will:
- Improve code consistency and maintainability
- Enable idiomatic Kotlin features (data classes, null safety, extension functions)
- Reduce boilerplate code
- Simplify developer onboarding

## What Changes

### Module Breakdown

| Module | Java Files | Description |
|--------|-----------|-------------|
| auth-api | 106 | DTOs, API interfaces, routes, mappers, validators |
| auth-data-postgres-client | 63 | JPA entities, repositories, services, mappers |
| auth-authorization-server-api | 12 | OAuth2 DTOs, routes, validators |
| auth-single-server | 2 | Configuration and route aggregation |
| auth-data | 1 | Test file |

### Conversion Categories

1. **DTOs/Entities** - Convert to Kotlin data classes
2. **API Interfaces** - Convert to Kotlin interfaces
3. **Routes/Controllers** - Convert preserving JAX-RS annotations
4. **Mappers** - Convert to Kotlin object or extension functions
5. **Validators** - Convert custom annotations and validators
6. **Repositories** - Convert Spring Data interfaces
7. **Services** - Convert to Kotlin classes with proper null handling
8. **Tests** - Convert JUnit tests to Kotlin

## Impact

- **Affected modules**: auth-api, auth-data-postgres-client, auth-authorization-server-api, auth-single-server, auth-data
- **No behavioral changes**: This is a pure refactoring effort
- **Build configuration**: May require Kotlin plugin updates in module pom.xml files
- **Testing**: Full test suite must pass after each phase
