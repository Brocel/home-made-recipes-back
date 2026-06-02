# OAuth2 Implementation Roadmap

**Document Version:** 1.0  
**Date:** 2026-06-02  
**Status:** Future Planning  
**Priority:** MEDIUM

---

## Executive Summary

This document outlines the implementation path for OAuth2 authentication in the Home Made Recipes backend. OAuth2 is currently NOT implemented—the application uses JWT authentication. This roadmap provides guidance for when OAuth2 becomes a requirement.

**Current Status:** ✅ JWT authentication working  
**OAuth2 Status:** ⏳ PENDING (awaiting architectural decision)  
**Timeline:** TBD (depends on feature prioritization)

---

## What is OAuth2 in This Context?

OAuth2 enables users to authenticate using third-party providers (Google, GitHub, etc.) instead of managing local credentials. This roadmap covers implementing OAuth2 as an **additional authentication method** alongside the existing JWT system.

### Current Authentication
```
User → Login → Generate JWT → Access Protected Resources
```

### Future with OAuth2
```
User → Auth with Google/GitHub → Generate JWT → Access Protected Resources
OR
User → Use existing JWT → Access Protected Resources
```

---

## Prerequisites & Decisions

Before implementing OAuth2, the following must be decided:

### 1. OAuth2 Provider Selection (REQUIRED)

| Provider | Pros | Cons | Recommendation |
|----------|------|------|-----------------|
| **Google** | Large user base, reliable, SDKs mature, easy setup | Privacy concerns, requires Google account | ✅ Good for general users |
| **GitHub** | Developer-friendly, popular for tech products | Limited to GitHub users | ✅ Good if user base is developers |
| **Custom (Keycloak/Auth0)** | Full control, independent, multi-tenant | Complex to set up and maintain | ⚠️ For enterprise needs |
| **Multiple (Google + GitHub)** | Flexibility for users | More configuration | ✅ Best UX |

**Recommendation:** Start with **Google** for broad user base, optionally add **GitHub** for developer users.

---

### 2. Integration Scope (REQUIRED)

Decide what OAuth2 will be used for:

- **Option A:** Authentication ONLY (replace JWT login)
- **Option B:** Authentication + Profile Data (sync user profile from provider)
- **Option C:** Authentication + Sharing (allow sharing via provider identity)

**Recommendation:** Option A (Authentication only) for initial release, extend to B/C later.

---

### 3. Logo/Branding (REQUIRED)

- [ ] Obtain provider logos and usage guidelines
- [ ] Design "Sign in with Google/GitHub" buttons
- [ ] Follow provider brand guidelines
- [ ] Test button appearance in light/dark modes

---

## Implementation Phases

### Phase 0: Preparation (Week 1)
**Duration:** 1-2 days  
**Responsible:** Architecture Team

**Tasks:**
- [ ] Decision: Select OAuth2 provider(s) (Google, GitHub, or both)
- [ ] Decision: Determine integration scope (auth only, or include profile sync)
- [ ] Obtain provider credentials:
  - [ ] Create OAuth2 application in provider console
  - [ ] Get Client ID and Client Secret
  - [ ] Register redirect URIs (e.g., `http://localhost:8080/login/oauth2/code/google`)
- [ ] Create GitHub issue(s) for implementation tracking
- [ ] Brief frontend team on requirements

**Output:**
- Provider credentials in secure vault
- Architectural decision document
- Implementation acceptance criteria

---

### Phase 1: Backend OAuth2 Configuration (Week 1-2)
**Duration:** 2-3 hours  
**Responsible:** Backend Team

**Tasks:**

#### 1.1 Add Spring Security OAuth2 Dependency
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

#### 1.2 Update Configuration Files
```yaml
# application.yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${OAUTH2_GOOGLE_CLIENT_ID}
            client-secret: ${OAUTH2_GOOGLE_CLIENT_SECRET}
            scope:
              - openid
              - profile
              - email
```

#### 1.3 Create OAuth2 Success Handler
- Handle successful authentication
- Create/update user in database
- Generate JWT token
- Redirect with token to frontend

#### 1.4 Implement User Synchronization
- Map OAuth2 user info to local User entity
- Handle first-time login (create user)
- Handle existing user (link OAuth2 identity)
- Store provider identity for future logins

#### 1.5 Update Error Handling
- Handle OAuth2 authentication failures
- Comprehensive error messages
- Logging for debugging

---

### Phase 2: Frontend Integration (Week 2-3)
**Duration:** 2-3 hours  
**Responsible:** Frontend Team

**Tasks:**
- [ ] Add "Sign in with Google/GitHub" buttons to login page
- [ ] Handle OAuth2 redirect callback
- [ ] Store JWT token from backend after OAuth2 auth
- [ ] Display user profile from OAuth2 or local database
- [ ] Handle logout (clear JWT token)
- [ ] Test OAuth2 flow end-to-end

---

### Phase 3: Security Hardening (Week 3)
**Duration:** 2-4 hours  
**Responsible:** Security Team + Backend Team

**Tasks:**
- [ ] Implement PKCE (Proof Key for Code Exchange)
- [ ] Add CSRF protection to OAuth2 flow
- [ ] Validate OAuth2 tokens before creating JWT
- [ ] Add rate limiting to OAuth2 endpoints
- [ ] Implement login anomaly detection
- [ ] Add audit logging for OAuth2 authentication events
- [ ] Create security test cases

---

### Phase 4: Testing & QA (Week 3-4)
**Duration:** 2-3 days  
**Responsible:** QA + Backend Team

**Tasks:**
- [ ] Unit tests for OAuth2 user mapping
- [ ] Integration tests for OAuth2 flow
- [ ] E2E tests for complete login workflow
- [ ] Test with multiple user scenarios
- [ ] Test error paths (invalid credentials, network failure)
- [ ] Performance testing under load
- [ ] Security testing (token expiration, refresh, revocation)

---

### Phase 5: Documentation & Deployment (Week 4)
**Duration:** 1-2 days  
**Responsible:** Backend Team + DevOps

**Tasks:**
- [ ] Document OAuth2 setup for developers
- [ ] Create deployment guide for OAuth2 credentials
- [ ] Document environment variables needed
- [ ] Create runbook for troubleshooting OAuth2 issues
- [ ] Deploy to staging environment
- [ ] Conduct UAT with real OAuth2 providers
- [ ] Deploy to production

---

## Configuration Checklist

### Environment Variables (Production)
```bash
# OAuth2 Provider Credentials
export OAUTH2_GOOGLE_CLIENT_ID="..."
export OAUTH2_GOOGLE_CLIENT_SECRET="..."
export OAUTH2_GITHUB_CLIENT_ID="..." # if using GitHub
export OAUTH2_GITHUB_CLIENT_SECRET="..." # if using GitHub

# OAuth2 Redirect URIs (must match provider configuration)
export OAUTH2_GOOGLE_REDIRECT_URI="https://recipes.example.com/login/oauth2/code/google"
export OAUTH2_GITHUB_REDIRECT_URI="https://recipes.example.com/login/oauth2/code/github"

# PKCE Configuration
export OAUTH2_PKCE_ENABLED="true"
```

### Local Development
```bash
# .env.local (not committed)
OAUTH2_GOOGLE_CLIENT_ID="xxx-local.apps.googleusercontent.com"
OAUTH2_GOOGLE_CLIENT_SECRET="GOCSPX-xxx"
OAUTH2_GOOGLE_REDIRECT_URI="http://localhost:8080/login/oauth2/code/google"
OAUTH2_GITHUB_CLIENT_ID="Iv1.xxx"
OAUTH2_GITHUB_CLIENT_SECRET="ghu_xxx"
OAUTH2_GITHUB_REDIRECT_URI="http://localhost:8080/login/oauth2/code/github"
```

### application.yaml Configuration
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${OAUTH2_GOOGLE_CLIENT_ID}
            client-secret: ${OAUTH2_GOOGLE_CLIENT_SECRET}
            scope:
              - openid
              - profile
              - email
            redirect-uri: ${OAUTH2_GOOGLE_REDIRECT_URI:http://localhost:8080/login/oauth2/code/google}
          github:
            client-id: ${OAUTH2_GITHUB_CLIENT_ID}
            client-secret: ${OAUTH2_GITHUB_CLIENT_SECRET}
            scope:
              - read:user
              - user:email
            redirect-uri: ${OAUTH2_GITHUB_REDIRECT_URI:http://localhost:8080/login/oauth2/code/github}
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/auth
            token-uri: https://www.googleapis.com/oauth2/v4/token
            user-info-uri: https://www.googleapis.com/oauth2/v2/userinfo
            user-name-attribute: id
          github:
            authorization-uri: https://github.com/login/oauth/authorize
            token-uri: https://github.com/login/oauth/access_token
            user-info-uri: https://api.github.com/user
            user-name-attribute: id
```

---

## Implementation Files to Create/Modify

### New Files
```
app/src/main/java/com/example/hmrback/security/oauth2/
├── OAuth2UserService.java              # Map OAuth2 user to JPA User
├── OAuth2LoginSuccessHandler.java      # Handle OAuth2 login success
├── OAuth2LoginFailureHandler.java      # Handle OAuth2 login failure
└── PrincipalDetails.java               # OAuth2 principal info holder

common/src/main/java/com/example/hmrback/oauth2/
├── OAuth2Provider.java                 # Enum: GOOGLE, GITHUB
└── OAuth2UserInfo.java                 # DTO for OAuth2 user data
```

### Modified Files
```
app/src/main/java/com/example/hmrback/config/SecurityConfig.java
  - Add OAuth2 client configuration
  - Configure OAuth2 success/failure handlers
  - Link JWT generator to OAuth2 handler

app/src/main/java/com/example/hmrback/api/controller/auth/AuthController.java
  - Add OAuth2 login endpoint (may be auto-handled by Spring)
  - Update logout endpoint to handle OAuth2 tokens

persistence/src/main/java/com/example/hmrback/persistence/entity/User.java
  - Add OAuth2 provider identity fields:
    - String oauth2Provider
    - String oauth2ProviderId
    - LocalDateTime oauth2ConnectedAt

app/src/main/resources/application.yaml
  - Add spring.security.oauth2 configuration (already prepared)
```

---

## User Entity Modifications

When OAuth2 is implemented, the User entity should support linking local and OAuth2 identities:

```java
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String email;
    private String password;  // Optional if OAuth2-only
    
    // OAuth2 Identity (NEW)
    @Enumerated(EnumType.STRING)
    private OAuth2Provider oauth2Provider;  // GOOGLE, GITHUB (optional)
    
    private String oauth2ProviderId;       // Provider-specific ID
    private LocalDateTime oauth2ConnectedAt;  // When linked to OAuth2
    
    // ... other fields
}
```

---

## Error Handling & Edge Cases

### Scenario 1: User Signs Up with OAuth2, Then Tries Local Login
```
User registers via Google → System creates local User record
User tries to login with email/password → NOT ALLOWED
Solution: Suggest "Sign in with Google" instead
```

### Scenario 2: User Signs Up Locally, Then Tries OAuth2
```
User registers with email/password → System creates User
User tries "Sign in with Google" same email → Should link accounts
Solution: Detect email match, prompt to confirm linking
```

### Scenario 3: OAuth2 Provider API Unavailable
```
User clicks "Sign in with Google" → Google unreachable
Solution: Show error page, suggest retry or local login
```

### Scenario 4: User Revokes OAuth2 Authorization
```
User revokes app access in Google Settings → Next login fails
Solution: Treat as new OAuth2 attempt, ask for permission again
```

---

## Rollback Strategy

If OAuth2 implementation has issues:

1. **Day 1-2:** Disable OAuth2 in config (set `spring.security.oauth2.client.registration.google.enabled=false`)
2. **Day 3:** If unresolved, revert to commit before OAuth2 changes
3. **Keep:** User OAuth2 identity records (for future re-enablement)
4. **Communication:** Notify users of temporary OAuth2 unavailability

---

## Timeline Estimate

| Phase | Task | Duration | Start Week |
|-------|------|----------|-----------|
| 0 | Preparation & decisions | 1-2 days | Week 1 |
| 1 | Backend OAuth2 config | 2-3 hours | Week 1 |
| 2 | Frontend integration | 2-3 hours | Week 2 |
| 3 | Security hardening | 2-4 hours | Week 3 |
| 4 | Testing & QA | 2-3 days | Week 3 |
| 5 | Documentation & deploy | 1-2 days | Week 4 |
| **TOTAL** | **OAuth2 Implementation** | **~2 weeks** | |

---

## Success Criteria

- ✅ Users can sign in with Google (if selected)
- ✅ Users can sign in with GitHub (if selected)  
- ✅ OAuth2 credentials securely stored in environment variables
- ✅ User profile auto-populated from OAuth2 provider
- ✅ JWT token generated after OAuth2 authentication
- ✅ All security best practices implemented (PKCE, CSRF, etc.)
- ✅ Zero downtime deployment
- ✅ Frontend successfully receives JWT and can access API
- ✅ Comprehensive logging for troubleshooting
- ✅ Performance: < 500ms from OAuth2 redirect to JWT

---

## Future Enhancements

### Phase 6+: Advanced OAuth2 Features (Future)
- [ ] Social identity linking (link multiple providers to one user)
- [ ] WebAuthn/FIDO2 authentication
- [ ] Passwordless login with magic links
- [ ] Two-factor authentication (2FA)
- [ ] OAuth2 scopes for API access (not just user authentication)
- [ ] Single Sign-Out (SLO) across multiple apps

---

## Decision Log

**Decision 1:** OAuth2 implementation postponed until architectural decision made  
**Date:** 2026-06-02  
**Reason:** Focus on immediate quality improvements (CORS, error handling, flyway consolidation)  
**Next Review:** When product roadmap requires OAuth2 functionality

---

## References & Useful Links

### Providers
- https://developers.google.com/identity/protocols/oauth2/web-server-flow
- https://github.com/settings/applications
- https://auth0.com/docs/get-started

### Spring Security OAuth2
- https://spring.io/projects/spring-security-oauth2-client
- https://spring.io/guides/tutorials/spring-security-and-angular-js/

### PKCE (Proof Key for Code Exchange)
- https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow-with-proof-key-for-code-exchange-pkce

### Best Practices
- https://datatracker.ietf.org/doc/html/rfc6749 (OAuth2 Spec)
- https://datatracker.ietf.org/doc/html/rfc7636 (PKCE)

---

## Contact & Questions

For questions about OAuth2 implementation:
- Backend Team Lead: [TBD]
- Security Lead: [TBD]
- Product Manager: [TBD]

---

## Document History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2026-06-02 | GitHub Copilot | Initial roadmap created |

**Next Review Date:** When OAuth2 becomes a prioritized feature

---

**Status:** ⏳ PENDING ARCHITECTURAL DECISION  
**Current Authentication:** ✅ JWT (Working)  
**OAuth2 Status:** ⏳ Planned for future implementation  
**Ready to Start:** Once provider selection decision made

