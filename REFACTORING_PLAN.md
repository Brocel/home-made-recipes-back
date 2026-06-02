# App Module Refactoring Plan

Based on comprehensive module review - Quick Wins Phase (4 hours total)

## Step 1: Merge Flyway Configurations ✅ (IN PROGRESS)
**Duration:** 1-2 hours | **Priority:** HIGH | **Risk:** LOW

### Objectives
- Eliminate code duplication between `FlywayConfig.java` and `FlywayLocalConfig.java`
- Consolidate profile-specific logic into single configuration class
- Use Spring's property resolution to handle location differences
- Maintain backward compatibility with profiles

### Tasks

#### 1.1 Extract Flyway locations to application properties
- Add `flyway.locations.migration` and `flyway.locations.local` to base config
- Update profile-specific configs with appropriate location lists

#### 1.2 Create unified FlywayConfig
- Merge FlywayConfig and FlywayLocalConfig into single @Configuration
- Use @ConditionalOnProfile to handle profile switching
- Inject locations from properties

#### 1.3 Delete duplicate configuration
- Remove FlywayLocalConfig.java
- Clean up pom.xml if any profile-specific dependencies exist

#### 1.4 Verify and test
- Run application with `prod` profile → should migrate from `db/migration` only
- Run application with `local` profile → should migrate from `db/migration` + `db/local`
- Run existing tests → all should pass

### Acceptance Criteria
- ✓ Two Flyway config files replaced with one
- ✓ No code duplication (0% similarity)
- ✓ All profiles work correctly
- ✓ Existing tests pass
- ✓ No breaking changes to migration behavior
- ✓ Configuration properties are typed and validated

### Files to Modify
1. `app/src/main/resources/application.yaml` - Add flyway locations config
2. `app/src/main/resources/application-local.yaml` - Override locations
3. `app/src/main/java/.../config/FlywayConfig.java` - Merge both configs
4. Delete: `app/src/main/java/.../config/FlywayLocalConfig.java`

### Verification Steps
- [ ] Application starts with `--spring.profiles.active=prod`
- [ ] Application starts with `--spring.profiles.active=local`
- [ ] Migration directory structure confirmed: `src/main/resources/db/migration/` and `db/local/`
- [ ] No SQL migration issues
- [ ] Unit tests pass

---

## Step 2: Fix CORS Header Configuration (PENDING)
**Duration:** 30 minutes | **Priority:** HIGH | **Risk:** MEDIUM

### Objectives
- Replace CORS wildcard headers with explicit whitelist
- Fix CORS spec violation (allowCredentials + wildcard)
- Document allowed headers for API consumers

### Files to Modify
1. `app/src/main/java/.../config/SecurityConfig.java` - Update corsConfigurationSource()
2. Extract allowed headers to @ConfigurationProperties or constant

### Acceptance Criteria
- ✓ CORS header wildcard removed
- ✓ Explicit header whitelist documented
- ✓ CORS spec compliance verified
- ✓ Integration tests validate CORS headers
- ✓ No client-side regressions

---

## Step 3: Unify Error Response Format (PENDING)
**Duration:** 1-1.5 hours | **Priority:** HIGH | **Risk:** MEDIUM

### Objectives
- Make CustomAuthenticationEntryPoint use ApiError format
- Remove French hard-coded error message
- Standardize error responses across all security exceptions
- Integrate message source for i18n

### Files to Modify
1. `app/src/main/java/.../exception/handler/CustomAuthenticationEntryPoint.java` - Use ApiError
2. `app/src/main/java/.../exception/handler/GlobalExceptionHandler.java` - May need adjustments
3. Add message properties for i18n

### Acceptance Criteria
- ✓ All error responses use ApiError record
- ✓ Timestamp included in all errors
- ✓ Hard-coded messages removed
- ✓ Error format consistency verified in tests
- ✓ i18n framework prepared for future translations

---

## Step 4: Clean Up YAML TODOs (PENDING)
**Duration:** 30 minutes | **Priority:** MEDIUM | **Risk:** LOW

### Objectives
- Document OAuth2 TODO status
- Mark incomplete configurations with issue links
- Prepare for future OAuth2 implementation

### Files to Modify
1. `app/src/main/resources/application.yaml` - Update TODO comments

### Acceptance Criteria
- ✓ TODOs are clear about nature and dependencies
- ✓ No ambiguous incomplete configurations left
- ✓ Documentation references GitHub issues (if applicable)

---

## Timeline Summary

| Step | Task | Duration | Status |
|------|------|----------|--------|
| 1 | Merge Flyway Configs | 1-2 hrs | 🔄 IN PROGRESS |
| 2 | Fix CORS Headers | 30 min | ⏳ PENDING |
| 3 | Unify Error Response | 1-1.5 hrs | ⏳ PENDING |
| 4 | Clean TODOs | 30 min | ⏳ PENDING |
| **TOTAL** | **Quick Wins Phase** | **~4 hrs** | |

---

## Notes
- Each step is independent after Step 1
- All changes preserve backward compatibility
- Test coverage increases with each step
- No breaking changes to API contracts

