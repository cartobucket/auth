z`# Change: Refactor Project into Three Core Modules

## Why

The current module structure has five modules with overlapping concerns and unclear boundaries. The `auth-api` and `auth-authorization-server-api` modules both handle web/HTTP concerns but are separate, while `auth-data` and `auth-data-postgres-client` have clear separation but inconsistent naming. Consolidating into three well-defined modules (`core`, `persistence`, `web`) will:

- Simplify the dependency graph
- Make the architecture easier to understand
- Establish clear responsibility boundaries
- Reduce cognitive overhead when navigating the codebase

## What Changes

### Module Consolidation

| Current Module | Target Module | Contents |
|----------------|---------------|----------|
| `auth-data` | `core` | Domain classes, service interfaces, exceptions |
| `auth-api` (DTOs, interfaces) | `core` | REST DTOs, API interfaces |
| `auth-authorization-server-api` (DTOs, interfaces) | `core` | OAuth/OIDC DTOs, API interface |
| `auth-data-postgres-client` | `persistence` | JPA entities, Panache repositories, service implementations, entity mappers |
| `auth-api` (routes, mappers, validators) | `web` | Route implementations, route mappers, validators |
| `auth-authorization-server-api` (routes, mappers, validators) | `web` | OAuth route implementation, mappers, validators |
| `auth-single-server` | `web` | Main application entry point, configuration (merged into web) |

### New Dependency Graph

```
web (application)
├── persistence
│   └── core
└── core
```

### Package Structure Changes

**core module:**
```
com.revethq.auth.core.domain.*                     (domain classes)
com.revethq.auth.core.services.*                   (service interfaces)
com.revethq.auth.core.exceptions.*                 (exceptions)
com.revethq.auth.core.api.dto.*                    (REST API DTOs)
com.revethq.auth.core.api.interfaces.*             (REST API interfaces)
com.revethq.auth.core.authorization.dto.*          (OAuth/OIDC DTOs)
com.revethq.auth.core.authorization.interfaces.*   (OAuth API interface)
```

**persistence module:**
```
com.revethq.auth.persistence.entities.*         (JPA entities)
com.revethq.auth.persistence.entities.mappers.* (entity mappers)
com.revethq.auth.persistence.repositories.*     (Panache repos)
com.revethq.auth.persistence.services.*         (service impls)
```

**web module:**

The web module contains route implementations for two distinct APIs:

*API routes (from auth-api):*
```
com.revethq.auth.web.api.routes.*           (route implementations)
com.revethq.auth.web.api.routes.mappers.*   (DTO mappers)
com.revethq.auth.web.api.validators.*       (validators)
```

*Authorization routes (from auth-authorization-server-api):*
```
com.revethq.auth.web.authorization.routes.*         (OAuth route implementation)
com.revethq.auth.web.authorization.routes.mappers.* (OAuth mappers)
com.revethq.auth.web.authorization.validators.*     (OAuth validators)
```

## Impact

- **Affected specs:** None (no existing specs defined)
- **Affected code:** All modules except configuration files
- **Breaking changes:** None externally; internal package renames only
- **Build system:** `settings.gradle` and all `build.gradle` files need updates
- **Import statements:** All files will require import path updates

## Migration Strategy

1. Create new module directories with Gradle build files
2. Move files to new locations with updated package declarations
3. Update all import statements
4. Merge `auth-single-server` configuration into `web` module
5. Update `settings.gradle` to reflect new module names
6. Remove old modules including `auth-single-server`
7. Verify build and tests pass
