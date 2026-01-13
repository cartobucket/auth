## ADDED Requirements

### Requirement: Kotlin Codebase Standard
The codebase SHALL use Kotlin as the primary programming language for all modules.

#### Scenario: All source files are Kotlin
- **WHEN** examining the source code in auth-api, auth-data, auth-data-postgres-client, auth-authorization-server-api, and auth-single-server modules
- **THEN** all production and test source files SHALL be Kotlin (.kt) files

#### Scenario: Idiomatic Kotlin patterns
- **WHEN** converting Java classes to Kotlin
- **THEN** the code SHALL use idiomatic Kotlin patterns including:
  - Data classes for DTOs and value objects
  - Null-safe types where appropriate
  - Extension functions where they improve readability
  - Object declarations for stateless mappers

#### Scenario: Preserved functionality
- **WHEN** Java code is converted to Kotlin
- **THEN** all existing functionality SHALL be preserved
- **AND** all existing tests SHALL continue to pass
- **AND** all API contracts SHALL remain unchanged
