# Tasks: Refactor Module Structure

## 1. Create New Module Structure

- [x] 1.1 Create `core/` directory with Gradle build file
- [x] 1.2 Create `persistence/` directory with Gradle build file
- [x] 1.3 Create `web/` directory with Gradle build file
- [x] 1.4 Update `settings.gradle` to include new modules (keep old modules temporarily)

## 2. Migrate Core Module - Domain Layer

- [x] 2.1 Create source directory structure: `core/src/main/kotlin/com/revethq/auth/core/`
- [x] 2.2 Move domain classes from `auth-data/.../domain/` to `core/.../core/domain/`
- [x] 2.3 Move service interfaces from `auth-data/.../services/` to `core/.../core/services/`
- [x] 2.4 Move exceptions from `auth-data/.../exceptions/` to `core/.../core/exceptions/`
- [x] 2.5 Update package declarations (`com.revethq.auth.data.*` → `com.revethq.auth.core.*`)

## 3. Migrate Core Module - API Contracts

- [x] 3.1 Move DTOs from `auth-api/.../dto/` to `core/.../core/api/dto/`
- [x] 3.2 Move interfaces from `auth-api/.../interfaces/` to `core/.../core/api/interfaces/`
- [x] 3.3 Update package declarations (`com.revethq.auth.api.dto.*` → `com.revethq.auth.core.api.dto.*`)
- [x] 3.4 Update package declarations (`com.revethq.auth.api.interfaces.*` → `com.revethq.auth.core.api.interfaces.*`)

## 4. Migrate Core Module - Authorization Contracts

- [x] 4.1 Move DTOs from `auth-authorization-server-api/.../dto/` to `core/.../core/authorization/dto/`
- [x] 4.2 Move interface from `auth-authorization-server-api/.../interfaces/` to `core/.../core/authorization/interfaces/`
- [x] 4.3 Update package declarations (`com.revethq.auth.authorization.server.dto.*` → `com.revethq.auth.core.authorization.dto.*`)
- [x] 4.4 Update package declarations (`com.revethq.auth.authorization.server.interfaces.*` → `com.revethq.auth.core.authorization.interfaces.*`)
- [x] 4.5 Update imports referencing old packages
- [x] 4.6 Verify `core` module compiles: `./gradlew :core:compileKotlin`

## 5. Migrate Persistence Module

- [x] 5.1 Create source directory structure: `persistence/src/main/kotlin/com/revethq/auth/persistence/`
- [x] 5.2 Move entities from `auth-data-postgres-client/.../entities/` to `persistence/.../persistence/entities/`
- [x] 5.3 Move entity mappers from `auth-data-postgres-client/.../entities/mappers/` to `persistence/.../persistence/entities/mappers/`
- [x] 5.4 Move repositories from `auth-data-postgres-client/.../repositories/` to `persistence/.../persistence/repositories/`
- [x] 5.5 Move service implementations from `auth-data-postgres-client/.../services/` to `persistence/.../persistence/services/`
- [x] 5.6 Update package declarations (`com.revethq.auth.postgres.client.*` → `com.revethq.auth.persistence.*`)
- [x] 5.7 Update imports referencing `auth-data` packages to use `core` packages
- [x] 5.8 Verify `persistence` module compiles: `./gradlew :persistence:compileKotlin`

## 6. Migrate Web Module - API Routes

- [x] 6.1 Create source directory structure: `web/src/main/kotlin/com/revethq/auth/web/api/`
- [x] 6.2 Move routes from `auth-api/.../server/routes/` to `web/.../web/api/routes/`
- [x] 6.3 Move route mappers from `auth-api/.../server/routes/mappers/` to `web/.../web/api/routes/mappers/`
- [x] 6.4 Move validators from `auth-api/.../server/validators/` to `web/.../web/api/validators/`
- [x] 6.5 Move exception handlers from `auth-api/.../server/exceptions/` to `web/.../web/api/exceptions/`
- [x] 6.6 Update package declarations (`com.revethq.auth.api.server.*` → `com.revethq.auth.web.api.*`)
- [x] 6.7 Update imports to use `core` packages for DTOs and interfaces

## 7. Migrate Web Module - Authorization Routes

- [x] 7.1 Create source directory structure: `web/src/main/kotlin/com/revethq/auth/web/authorization/`
- [x] 7.2 Move routes from `auth-authorization-server-api/.../routes/` to `web/.../web/authorization/routes/`
- [x] 7.3 Move route mappers from `auth-authorization-server-api/.../routes/mappers/` to `web/.../web/authorization/routes/mappers/`
- [x] 7.4 Move validators from `auth-authorization-server-api/.../validators/` to `web/.../web/authorization/validators/`
- [x] 7.5 Update package declarations (`com.revethq.auth.authorization.server.*` → `com.revethq.auth.web.authorization.*`)
- [x] 7.6 Update imports to use `core` packages for DTOs and interfaces

## 8. Merge Application Configuration into Web

- [x] 8.1 Move `auth-single-server/src/main/resources/application.properties` to `web/src/main/resources/`
- [x] 8.2 Move `auth-single-server/src/main/openapi/` to `web/src/main/openapi/`
- [x] 8.3 Move any other resources from `auth-single-server` (templates, static files)
- [x] 8.4 Update `web/build.gradle` to include Quarkus application plugin
- [x] 8.5 Merge dependencies from `auth-single-server/build.gradle` into `web/build.gradle`
- [x] 8.6 Verify `web` module compiles and runs: `./gradlew :web:quarkusDev`

## 9. Cleanup

- [x] 9.1 Remove `auth-data/` directory
- [x] 9.2 Remove `auth-data-postgres-client/` directory
- [x] 9.3 Remove `auth-api/` directory
- [x] 9.4 Remove `auth-authorization-server-api/` directory
- [x] 9.5 Remove `auth-single-server/` directory
- [x] 9.6 Update `settings.gradle` to remove old module references
- [x] 9.7 Run full test suite: `./gradlew test`
- [x] 9.8 Verify application starts correctly
- [x] 9.9 Run Spotless to ensure code formatting: `./gradlew spotlessApply`

## 10. Documentation

- [x] 10.1 Update `openspec/project.md` to reflect new module structure
- [x] 10.2 Update any README files referencing old module names
