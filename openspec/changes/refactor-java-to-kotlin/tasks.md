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

## Phase 3: auth-api (106 files)

Largest module - break into sub-phases.

### 3.1 DTOs - Request/Response Classes (32 files)
- [ ] Convert `ApplicationRequest.java`
- [ ] Convert `ApplicationRequestFilter.java`
- [ ] Convert `ApplicationResponse.java`
- [ ] Convert `ApplicationsResponse.java`
- [ ] Convert `ApplicationSecretRequest.java`
- [ ] Convert `ApplicationSecretResponse.java`
- [ ] Convert `ApplicationSecretsResponse.java`
- [ ] Convert `AuthorizationServerRequest.java`
- [ ] Convert `AuthorizationServerResponse.java`
- [ ] Convert `AuthorizationServersResponse.java`
- [ ] Convert `ClientRequest.java`
- [ ] Convert `ClientRequestFilter.java`
- [ ] Convert `ClientResponse.java`
- [ ] Convert `ClientsResponse.java`
- [ ] Convert `GroupRequest.java`
- [ ] Convert `GroupRequestFilter.java`
- [ ] Convert `GroupResponse.java`
- [ ] Convert `GroupsResponse.java`
- [ ] Convert `GroupMemberRequest.java`
- [ ] Convert `GroupMemberResponse.java`
- [ ] Convert `GroupMembersResponse.java`
- [ ] Convert `SchemaRequest.java`
- [ ] Convert `SchemaRequestFilter.java`
- [ ] Convert `SchemaResponse.java`
- [ ] Convert `SchemasResponse.java`
- [ ] Convert `ScopeRequest.java`
- [ ] Convert `ScopeRequestFilter.java`
- [ ] Convert `ScopeResponse.java`
- [ ] Convert `ScopesResponse.java`
- [ ] Convert `TemplateRequest.java`
- [ ] Convert `TemplateRequestFilter.java`
- [ ] Convert `TemplateResponse.java`
- [ ] Convert `TemplatesResponse.java`
- [ ] Convert `UserRequest.java`
- [ ] Convert `UserRequestFilter.java`
- [ ] Convert `UserResponse.java`
- [ ] Convert `UsersResponse.java`

### 3.2 DTOs - Utility Classes (6 files)
- [ ] Convert `Metadata.java`
- [ ] Convert `MetadataIdentifiersInner.java`
- [ ] Convert `MetadataSchemaValidationsInner.java`
- [ ] Convert `Page.java`
- [ ] Convert `TemplateTypeEnum.java`

### 3.3 API Interfaces (9 files)
- [ ] Convert `ApplicationsApi.java`
- [ ] Convert `ApplicationSecretsApi.java`
- [ ] Convert `AuthorizationServersApi.java`
- [ ] Convert `ClientsApi.java`
- [ ] Convert `GroupsApi.java`
- [ ] Convert `GroupMembersApi.java`
- [ ] Convert `SchemasApi.java`
- [ ] Convert `ScopesApi.java`
- [ ] Convert `TemplatesApi.java`
- [ ] Convert `UsersApi.java`

### 3.4 Routes (12 files)
- [ ] Convert `Applications.java`
- [ ] Convert `ApplicationSecrets.java`
- [ ] Convert `AuthorizationServers.java`
- [ ] Convert `Clients.java`
- [ ] Convert `Groups.java`
- [ ] Convert `GroupMembers.java`
- [ ] Convert `Schemas.java`
- [ ] Convert `Scopes.java`
- [ ] Convert `Templates.java`
- [ ] Convert `Users.java`
- [ ] Convert `Constants.java`
- [ ] Convert `Pagination.java`

### 3.5 Mappers (14 files)
- [ ] Convert `ApplicationMapper.java`
- [ ] Convert `ApplicationRequestFilterMapper.java`
- [ ] Convert `AuthorizationServerMapper.java`
- [ ] Convert `ClientMapper.java`
- [ ] Convert `ClientRequestFilterMapper.java`
- [ ] Convert `GroupMapper.java`
- [ ] Convert `GroupMemberMapper.java`
- [ ] Convert `GroupRequestFilterMapper.java`
- [ ] Convert `MetadataMapper.java`
- [ ] Convert `ProfileMapper.java`
- [ ] Convert `SchemaRequestFilterMapper.java`
- [ ] Convert `ScopeMapper.java`
- [ ] Convert `ScopeRequestFilterMapper.java`
- [ ] Convert `TemplateMapper.java`
- [ ] Convert `TemplateRequestFilterMapper.java`
- [ ] Convert `UserMapper.java`
- [ ] Convert `UserRequestFilterMapper.java`
- [ ] Convert `SchemaMapper.java`

### 3.6 Validators - Annotations (23 files)
- [ ] Convert `ValidApplication.java`
- [ ] Convert `ValidApplicationClientId.java`
- [ ] Convert `ValidApplicationClientSecret.java`
- [ ] Convert `ValidApplicationName.java`
- [ ] Convert `ValidApplicationProfile.java`
- [ ] Convert `ValidApplicationRequest.java`
- [ ] Convert `ValidApplicationSecret.java`
- [ ] Convert `ValidApplicationSecretName.java`
- [ ] Convert `ValidApplicationSecretScopes.java`
- [ ] Convert `ValidAuthorizationServer.java`
- [ ] Convert `ValidAuthorizationServerAudience.java`
- [ ] Convert `ValidAuthorizationServerName.java`
- [ ] Convert `ValidAuthorizationServerUrl.java`
- [ ] Convert `ValidClientName.java`
- [ ] Convert `ValidClientRedirectUris.java`
- [ ] Convert `ValidClientScopes.java`
- [ ] Convert `ValidEmail.java`
- [ ] Convert `ValidSchema.java`
- [ ] Convert `ValidSchemaName.java`
- [ ] Convert `ValidScopeName.java`
- [ ] Convert `ValidTemplate.java`
- [ ] Convert `ValidUsername.java`
- [ ] Convert `ValidUserProfile.java`

### 3.7 Exceptions (1 file)
- [ ] Convert `IllegalArgumentExceptionMapper.java`

### 3.8 Phase 3 Verification
- [ ] Run tests for auth-api
- [ ] Verify build succeeds

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
