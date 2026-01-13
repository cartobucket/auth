# Tasks: Java to Kotlin Conversion

## Phase 1: auth-authorization-server-api (12 files)

Smallest module - establish conversion patterns here first.

### 1.1 DTOs (6 files)
- [ ] Convert `AccessTokenRequest.java` to Kotlin data class
- [ ] Convert `AccessTokenResponse.java` to Kotlin data class
- [ ] Convert `AuthorizationRequest.java` to Kotlin data class
- [ ] Convert `JWK.java` to Kotlin data class
- [ ] Convert `JWKS.java` to Kotlin data class
- [ ] Convert `WellKnown.java` to Kotlin data class

### 1.2 Interface (1 file)
- [ ] Convert `AuthorizationServerApi.java` to Kotlin interface

### 1.3 Routes (1 file)
- [ ] Convert `AuthorizationServer.java` to Kotlin class

### 1.4 Mappers (2 files)
- [ ] Convert `AccessTokenResponseMapper.java` to Kotlin
- [ ] Convert `JwksMapper.java` to Kotlin

### 1.5 Validators (2 files)
- [ ] Convert `ValidAccessTokenRequest.java` to Kotlin annotation
- [ ] Convert `ValidAuthorizationServer.java` to Kotlin annotation

### 1.6 Phase 1 Verification
- [ ] Run tests for auth-authorization-server-api
- [ ] Verify build succeeds

---

## Phase 2: auth-single-server (2 files)

### 2.1 Configuration
- [ ] Convert `JsonbConfiguration.java` to Kotlin

### 2.2 Routes
- [ ] Convert `AuthorizationServerRoutes.java` to Kotlin

### 2.3 Phase 2 Verification
- [ ] Run tests for auth-single-server
- [ ] Verify build succeeds

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

## Phase 4: auth-data-postgres-client (63 files)

### 4.1 JPA Entities (17 files)
- [ ] Convert `AccessToken.java`
- [ ] Convert `Application.java`
- [ ] Convert `ApplicationSecret.java`
- [ ] Convert `AuthorizationServer.java`
- [ ] Convert `Client.java`
- [ ] Convert `ClientCode.java`
- [ ] Convert `Event.java`
- [ ] Convert `Group.java`
- [ ] Convert `GroupMember.java`
- [ ] Convert `IdentityProvider.java`
- [ ] Convert `JWK.java`
- [ ] Convert `JWKS.java`
- [ ] Convert `Profile.java`
- [ ] Convert `RefreshToken.java`
- [ ] Convert `Schema.java`
- [ ] Convert `Scope.java`
- [ ] Convert `ScopeReference.java`
- [ ] Convert `SigningKey.java`
- [ ] Convert `Template.java`
- [ ] Convert `User.java`

### 4.2 Repositories (15 files)
- [ ] Convert `ApplicationRepository.java`
- [ ] Convert `ApplicationSecretRepository.java`
- [ ] Convert `AuthorizationServerRepository.java`
- [ ] Convert `ClientCodeRepository.java`
- [ ] Convert `ClientRepository.java`
- [ ] Convert `EventRepository.java`
- [ ] Convert `GroupMemberRepository.java`
- [ ] Convert `GroupRepository.java`
- [ ] Convert `IdentityProviderRepository.java`
- [ ] Convert `ProfileRepository.java`
- [ ] Convert `RefreshTokenRepository.java`
- [ ] Convert `SchemaRepository.java`
- [ ] Convert `ScopeReferenceRepository.java`
- [ ] Convert `ScopeRepository.java`
- [ ] Convert `SingingKeyRepository.java`
- [ ] Convert `TemplateRepository.java`
- [ ] Convert `UserRepository.java`

### 4.3 Services (11 files)
- [ ] Convert `ApplicationService.java`
- [ ] Convert `AuthorizationServerService.java`
- [ ] Convert `ClientService.java`
- [ ] Convert `GroupMemberService.java`
- [ ] Convert `GroupService.java`
- [ ] Convert `IdentityProviderService.java`
- [ ] Convert `SchemaService.java`
- [ ] Convert `ScopeService.java`
- [ ] Convert `TemplateService.java`
- [ ] Convert `UserService.java`

### 4.4 Entity Mappers (15 files)
- [ ] Convert `ApplicationMapper.java`
- [ ] Convert `ApplicationSecretMapper.java`
- [ ] Convert `AuthorizationServerMapper.java`
- [ ] Convert `ClientCodeMapper.java`
- [ ] Convert `ClientMapper.java`
- [ ] Convert `GroupMapper.java`
- [ ] Convert `GroupMemberMapper.java`
- [ ] Convert `JWKMapper.java`
- [ ] Convert `ProfileMapper.java`
- [ ] Convert `SchemaMapper.java`
- [ ] Convert `ScopeMapper.java`
- [ ] Convert `SigningKeyMapper.java`
- [ ] Convert `TemplateMapper.java`
- [ ] Convert `UserMapper.java`

### 4.5 Misc (5 files)
- [ ] Convert `EventType.java` (enum)
- [ ] Convert `PasswordAuthRequest.java`

### 4.6 Phase 4 Verification
- [ ] Run tests for auth-data-postgres-client
- [ ] Verify build succeeds

---

## Phase 5: Cleanup & Final Verification

### 5.1 Remaining Files
- [ ] Convert `WellKnownEndpointsTests.java` (auth-data test)

### 5.2 Build Configuration
- [ ] Verify Kotlin plugin configuration in all module pom.xml files
- [ ] Remove Java source directories from modules if empty

### 5.3 Final Verification
- [ ] Run full test suite across all modules
- [ ] Verify application starts successfully
- [ ] Verify API endpoints respond correctly
- [ ] Clean up any remaining Java file references
