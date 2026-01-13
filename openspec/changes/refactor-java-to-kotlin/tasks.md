# Tasks: Java to Kotlin Conversion

## Phase 1: auth-authorization-server-api (12 files) ✅ COMPLETED

Smallest module - establish conversion patterns here first.

### 1.1 DTOs (6 files) ✅
- [x] Convert `AccessTokenRequest.java` to Kotlin data class
- [x] Convert `AccessTokenResponse.java` to Kotlin data class
- [x] Convert `AuthorizationRequest.java` to Kotlin data class
- [x] Convert `JWK.java` to Kotlin data class
- [x] Convert `JWKS.java` to Kotlin data class
- [x] Convert `WellKnown.java` to Kotlin data class

### 1.2 Interface (1 file) ✅
- [x] Convert `AuthorizationServerApi.java` to Kotlin interface

### 1.3 Routes (1 file) ✅
- [x] Convert `AuthorizationServer.java` to Kotlin class

### 1.4 Mappers (2 files) ✅
- [x] Convert `AccessTokenResponseMapper.java` to Kotlin
- [x] Convert `JwksMapper.java` to Kotlin

### 1.5 Validators (2 files) ✅
- [x] Convert `ValidAccessTokenRequest.java` to Kotlin annotation
- [x] Convert `ValidAuthorizationServer.java` to Kotlin annotation

### 1.6 Phase 1 Verification ✅
- [x] Run tests for auth-authorization-server-api
- [x] Verify build succeeds

---

## Phase 2: auth-single-server (2 files) ✅ COMPLETED

### 2.1 Configuration ✅
- [x] Convert `JsonbConfiguration.java` to Kotlin

### 2.2 Routes ✅
- [x] Convert `AuthorizationServerRoutes.java` to Kotlin

### 2.3 Phase 2 Verification ✅
- [x] Run tests for auth-single-server
- [x] Verify build succeeds

---

## Phase 3: auth-api (106 files) ✅ COMPLETED

Largest module - break into sub-phases.

### 3.1 DTOs - Request/Response Classes (32 files) ✅
- [x] Convert `ApplicationRequest.java`
- [x] Convert `ApplicationRequestFilter.java`
- [x] Convert `ApplicationResponse.java`
- [x] Convert `ApplicationsResponse.java`
- [x] Convert `ApplicationSecretRequest.java`
- [x] Convert `ApplicationSecretResponse.java`
- [x] Convert `ApplicationSecretsResponse.java`
- [x] Convert `AuthorizationServerRequest.java`
- [x] Convert `AuthorizationServerResponse.java`
- [x] Convert `AuthorizationServersResponse.java`
- [x] Convert `ClientRequest.java`
- [x] Convert `ClientRequestFilter.java`
- [x] Convert `ClientResponse.java`
- [x] Convert `ClientsResponse.java`
- [x] Convert `GroupRequest.java`
- [x] Convert `GroupRequestFilter.java`
- [x] Convert `GroupResponse.java`
- [x] Convert `GroupsResponse.java`
- [x] Convert `GroupMemberRequest.java`
- [x] Convert `GroupMemberResponse.java`
- [x] Convert `GroupMembersResponse.java`
- [x] Convert `SchemaRequest.java`
- [x] Convert `SchemaRequestFilter.java`
- [x] Convert `SchemaResponse.java`
- [x] Convert `SchemasResponse.java`
- [x] Convert `ScopeRequest.java`
- [x] Convert `ScopeRequestFilter.java`
- [x] Convert `ScopeResponse.java`
- [x] Convert `ScopesResponse.java`
- [x] Convert `TemplateRequest.java`
- [x] Convert `TemplateRequestFilter.java`
- [x] Convert `TemplateResponse.java`
- [x] Convert `TemplatesResponse.java`
- [x] Convert `UserRequest.java`
- [x] Convert `UserRequestFilter.java`
- [x] Convert `UserResponse.java`
- [x] Convert `UsersResponse.java`

### 3.2 DTOs - Utility Classes (6 files) ✅
- [x] Convert `Metadata.java`
- [x] Convert `MetadataIdentifiersInner.java`
- [x] Convert `MetadataSchemaValidationsInner.java`
- [x] Convert `Page.java`
- [x] Convert `TemplateTypeEnum.java`

### 3.3 API Interfaces (9 files) ✅
- [x] Convert `ApplicationsApi.java`
- [x] Convert `ApplicationSecretsApi.java`
- [x] Convert `AuthorizationServersApi.java`
- [x] Convert `ClientsApi.java`
- [x] Convert `GroupsApi.java`
- [x] Convert `GroupMembersApi.java`
- [x] Convert `SchemasApi.java`
- [x] Convert `ScopesApi.java`
- [x] Convert `TemplatesApi.java`
- [x] Convert `UsersApi.java`

### 3.4 Routes (12 files) ✅
- [x] Convert `Applications.java`
- [x] Convert `ApplicationSecrets.java`
- [x] Convert `AuthorizationServers.java`
- [x] Convert `Clients.java`
- [x] Convert `Groups.java`
- [x] Convert `GroupMembers.java`
- [x] Convert `Schemas.java`
- [x] Convert `Scopes.java`
- [x] Convert `Templates.java`
- [x] Convert `Users.java`
- [x] Convert `Constants.java`
- [x] Convert `Pagination.java`

### 3.5 Mappers (18 files) ✅
- [x] Convert `ApplicationMapper.java`
- [x] Convert `ApplicationRequestFilterMapper.java`
- [x] Convert `AuthorizationServerMapper.java`
- [x] Convert `ClientMapper.java`
- [x] Convert `ClientRequestFilterMapper.java`
- [x] Convert `GroupMapper.java`
- [x] Convert `GroupMemberMapper.java`
- [x] Convert `GroupRequestFilterMapper.java`
- [x] Convert `MetadataMapper.java`
- [x] Convert `ProfileMapper.java`
- [x] Convert `SchemaRequestFilterMapper.java`
- [x] Convert `ScopeMapper.java`
- [x] Convert `ScopeRequestFilterMapper.java`
- [x] Convert `TemplateMapper.java`
- [x] Convert `TemplateRequestFilterMapper.java`
- [x] Convert `UserMapper.java`
- [x] Convert `UserRequestFilterMapper.java`
- [x] Convert `SchemaMapper.java`

### 3.6 Validators - Annotations (23 files) ✅
- [x] Convert `ValidApplication.java`
- [x] Convert `ValidApplicationClientId.java`
- [x] Convert `ValidApplicationClientSecret.java`
- [x] Convert `ValidApplicationName.java`
- [x] Convert `ValidApplicationProfile.java`
- [x] Convert `ValidApplicationRequest.java`
- [x] Convert `ValidApplicationSecret.java`
- [x] Convert `ValidApplicationSecretName.java`
- [x] Convert `ValidApplicationSecretScopes.java`
- [x] Convert `ValidAuthorizationServer.java`
- [x] Convert `ValidAuthorizationServerAudience.java`
- [x] Convert `ValidAuthorizationServerName.java`
- [x] Convert `ValidAuthorizationServerUrl.java`
- [x] Convert `ValidClientName.java`
- [x] Convert `ValidClientRedirectUris.java`
- [x] Convert `ValidClientScopes.java`
- [x] Convert `ValidEmail.java`
- [x] Convert `ValidSchema.java`
- [x] Convert `ValidSchemaName.java`
- [x] Convert `ValidScopeName.java`
- [x] Convert `ValidTemplate.java`
- [x] Convert `ValidUsername.java`
- [x] Convert `ValidUserProfile.java`

### 3.7 Exceptions (1 file) ✅
- [x] Convert `IllegalArgumentExceptionMapper.java`

### 3.8 Phase 3 Verification ✅
- [x] Run tests for auth-api
- [x] Verify build succeeds

---

## Phase 4: auth-data-postgres-client (63 files) ✅ COMPLETED

### 4.1 JPA Entities (20 files) ✅
- [x] Convert `AccessToken.java`
- [x] Convert `Application.java`
- [x] Convert `ApplicationSecret.java`
- [x] Convert `AuthorizationServer.java`
- [x] Convert `Client.java`
- [x] Convert `ClientCode.java`
- [x] Convert `Event.java`
- [x] Convert `EventType.java` (enum)
- [x] Convert `Group.java`
- [x] Convert `GroupMember.java`
- [x] Convert `IdentityProvider.java`
- [x] Convert `JWK.java`
- [x] Convert `JWKS.java`
- [x] Convert `PasswordAuthRequest.java`
- [x] Convert `Profile.java`
- [x] Convert `RefreshToken.java`
- [x] Convert `Schema.java`
- [x] Convert `Scope.java`
- [x] Convert `ScopeReference.java`
- [x] Convert `SigningKey.java`
- [x] Convert `Template.java`
- [x] Convert `User.java`

### 4.2 Repositories (17 files) ✅
- [x] Convert `ApplicationRepository.java`
- [x] Convert `ApplicationSecretRepository.java`
- [x] Convert `AuthorizationServerRepository.java`
- [x] Convert `ClientCodeRepository.java`
- [x] Convert `ClientRepository.java`
- [x] Convert `EventRepository.java`
- [x] Convert `GroupMemberRepository.java`
- [x] Convert `GroupRepository.java`
- [x] Convert `IdentityProviderRepository.java`
- [x] Convert `ProfileRepository.java`
- [x] Convert `RefreshTokenRepository.java`
- [x] Convert `SchemaRepository.java`
- [x] Convert `ScopeReferenceRepository.java`
- [x] Convert `ScopeRepository.java`
- [x] Convert `SigningKeyRepository.java`
- [x] Convert `TemplateRepository.java`
- [x] Convert `UserRepository.java`

### 4.3 Services (10 files) ✅
- [x] Convert `ApplicationService.java`
- [x] Convert `AuthorizationServerService.java`
- [x] Convert `ClientService.java`
- [x] Convert `GroupMemberService.java`
- [x] Convert `GroupService.java`
- [x] Convert `IdentityProviderService.java`
- [x] Convert `SchemaService.java`
- [x] Convert `ScopeService.java`
- [x] Convert `TemplateService.java`
- [x] Convert `UserService.java`

### 4.4 Entity Mappers (14 files) ✅
- [x] Convert `ApplicationMapper.java`
- [x] Convert `ApplicationSecretMapper.java`
- [x] Convert `AuthorizationServerMapper.java`
- [x] Convert `ClientCodeMapper.java`
- [x] Convert `ClientMapper.java`
- [x] Convert `GroupMapper.java`
- [x] Convert `GroupMemberMapper.java`
- [x] Convert `JWKMapper.java`
- [x] Convert `ProfileMapper.java`
- [x] Convert `SchemaMapper.java`
- [x] Convert `ScopeMapper.java`
- [x] Convert `SigningKeyMapper.java`
- [x] Convert `TemplateMapper.java`
- [x] Convert `UserMapper.java`

### 4.5 Phase 4 Verification ✅
- [x] Delete old Java files
- [x] Run tests for auth-data-postgres-client
- [x] Verify build succeeds

---

## Phase 5: Cleanup & Final Verification ✅ COMPLETED

### 5.1 Remaining Files ✅
- [x] Convert `WellKnownEndpointsTests.java` (auth-data test)

### 5.2 Build Configuration ✅
- [x] Verify Kotlin plugin configuration in all module build.gradle files
- [x] Remove Java source directories from modules if empty

### 5.3 Final Verification ✅
- [x] Run full test suite across all modules
- [x] Verify build succeeds
- [x] Clean up any remaining Java file references

---

## Conversion Complete! 🎉

All Java files have been successfully converted to Kotlin across all modules:
- **auth-authorization-server-api**: 12 files converted
- **auth-single-server**: 2 files converted
- **auth-api**: 106 files converted
- **auth-data-postgres-client**: 63 files converted
- **auth-data**: 1 test file converted

Total: **184 files** converted from Java to Kotlin.
