# I18n Error Messages: Backend-Frontend Coordination Strategy

**Document Version:** 1.0  
**Date:** 2026-06-02  
**Status:** Design Review

---

## Executive Summary

The current `messages.properties` approach creates **duplication and coordination challenges** between backend and frontend i18n systems. This document analyzes the issue and recommends a solution that leverages your existing frontend i18n infrastructure (FR/pt-BR translations) instead of duplicating translations in the backend.

**Recommendation:** Implement **Option 4 (Error Keys + Arguments Pattern)** where the backend sends error codes and arguments, and the frontend handles all translations using its existing i18n system.

---

## The Problem

### Current Suboptimal Approach (messages.properties)

The `messages.properties` file created in Step 3 generates a **false i18n layer**:

```properties
# app/src/main/resources/messages.properties
AUTHENTICATION_REQUIRED=Authentication is required to access this resource.
RECIPE_NOT_FOUND_BY_ID=The recipe (id :: {0}) couldn't be found.
```

### Issues This Creates

#### 1. **Duplication of Translations**
- Backend has English error messages in `messages.properties`
- Frontend has its own message keys in JSON i18n files
- You must maintain translations in TWO places
- Updates require changes in both systems

#### 2. **Coordination Nightmare**
- New error codes must be added in:
  - `ExceptionMessageEnum.java` (backend)
  - `messages.properties` (backend)
  - Frontend i18n JSON (frontend)
- If you add FR/pt-BR to backend, they diverge from frontend
- No single source of truth

#### 3. **Conflicting Responsibility**
Frontend is already handling internationalization:
Frontend i18n file (e.g., en.json)
```json
{
  "exceptions": {
    "authRequired": "Authentication is required to access this resource.",
    "recipeNotFound": "The recipe couldn't be found."
  }
}
```

Backend should NOT duplicate this responsibility.

#### 4. **API Response Bloat**
Option with full messages (what messages.properties suggests)
```json
{
  "timestamp": "2026-06-02T...",
  "status": 401,
  "error": "AUTHENTICATION_REQUIRED",
  "message": "Authentication is required to access this resource.",  // ← Duplicated in frontend
  "path": "/hmr/api/recipes"
}
```

#### 5. **Language Negotiation Complexity**
- How does frontend know which language the backend will use?
- Accept-Language header adds complexity
- Frontend already knows the user's language preference
- Duplicating that decision in backend is wasteful

---

## Design Options Analysis

### Option 1: Backend Sends Full Translated Messages (❌ NOT Recommended)

**Response Format:**
```json
{
  "status": 401,
  "error": "AUTHENTICATION_REQUIRED",
  "message": "L'authentification est requise...",  // Translated based on Accept-Language
  "path": "/hmr/api/recipes"
}
```

**Pros:**
- ✅ Frontend needs no translation logic for errors
- ✅ Backend controls all translations

**Cons:**
- ❌ Duplicates frontend i18n system
- ❌ Backend must maintain FR, pt-BR, EN, and any other language
- ❌ Accept-Language header adds complexity
- ❌ Conflicts with frontend language preference handling
- ❌ Large API responses with full messages in every language scenario
- ❌ Backend becomes responsible for UI copy (violates separation of concerns)

**Verdict:** ❌ **Not recommended** - creates parallel i18n system unsustainable for multiple languages.

---

### Option 2: Frontend Has Custom Error Keys + Backend Sends Keys (✅ GOOD)

**Response Format:**
```json
{
  "status": 401,
  "error": "AUTHENTICATION_REQUIRED",
  "errorKey": "errors.auth.required",
  "messageArgs": null,
  "path": "/hmr/api/recipes"
}
```

**Frontend Usage:**
```typescript
// frontend i18n key mapping
const errorKeyMap: Record<string, string> = {
  "AUTHENTICATION_REQUIRED": "errors.auth.required",
  "RECIPE_NOT_FOUND_BY_ID": "errors.recipe.notFound",
  "ACCESS_DENIED": "errors.access.denied"
};

const handleError = (response) => {
  const i18nKey = errorKeyMap[response.error];
  const message = t(i18nKey);  // Use frontend i18n
  showError(message);
};
```

**Pros:**
- ✅ Single source of truth (frontend i18n)
- ✅ No backend translation maintenance
- ✅ Leverages existing frontend i18n infrastructure
- ✅ Frontend controls error UI text
- ✅ Clean separation of concerns

**Cons:**
- ❌ Frontend must know all possible error codes
- ❌ Frontend needs error code mapping maintained
- ⚠️ If backend adds new error, frontend must update
- ⚠️ Requires frontend and backend coordination on error codes

**Verdict:** ✅ **Good option** - simple, but requires frontend-backend coordination.

---

### Option 3: Hybrid - Backend Sends Key + Fallback English Message (✅ BEST FOR NOW)

**Response Format:**
```json
{
  "timestamp": "2026-06-02T...",
  "status": 401,
  "error": "AUTHENTICATION_REQUIRED",
  "errorKey": "errors.auth.required",
  "message": "Authentication is required to access this resource.",
  "messageArgs": null,
  "path": "/hmr/api/recipes"
}
```

**Frontend Usage:**
```typescript
const handleError = (response) => {
  // Try to translate using the provided error key
  if (response.errorKey && i18n.exists(response.errorKey)) {
    const message = t(response.errorKey, { args: response.messageArgs });
    showError(message);
  } else {
    // Fallback to backend message if no translation available
    showError(response.message);
  }
};
```

**Pros:**
- ✅ Leverages frontend i18n when available
- ✅ Graceful fallback if translation missing
- ✅ Improves with time (as more translations added)
- ✅ Frontend controls error UI text
- ✅ Backend provides English fallback (accessible)
- ✅ Non-breaking if frontend i18n is unavailable
- ✅ Supports parameterized messages

**Cons:**
- ⚠️ Slightly larger API responses (includes both key and message)
- ⚠️ Risk of fallback English being shown if i18n is broken
- ⚠️ Requires careful coordination of error keys

**Verdict:** ✅ **BEST OPTION** - Flexible, safe, incremental i18n support.

---

### Option 4: Backend Sends Only Error Key + Message Arguments (✅ ALSO GOOD)

**Response Format:**
```json
{
  "timestamp": "2026-06-02T...",
  "status": 401,
  "error": "AUTHENTICATION_REQUIRED",
  "errorKey": "errors.auth.required",
  "messageArgs": ["userId123"],
  "path": "/hmr/api/recipes"
}
```

**Frontend Usage (No message fallback):**
```typescript
const handleError = (response) => {
  const message = t(response.errorKey, { args: response.messageArgs });
  showError(message);
};
```

**Pros:**
- ✅ Minimal backend i18n responsibility
- ✅ Frontend own all UI text
- ✅ Clean separation of concerns
- ✅ Smaller API responses (no English message)
- ✅ Forces frontend to keep translations synchronized

**Cons:**
- ❌ No fallback if translation missing (blank error or crash)
- ❌ Requires all frontend translations before deployment
- ❌ Frontend completely responsible for error UX
- ❌ Less accessible if i18n fails (users see nothing or error code)

**Verdict:** ✅ **Good for mature i18n systems**, but risky without fallback.

---

## Comparison Matrix

| Aspect | Option 1 | Option 2 | **Option 3** | Option 4 |
|--------|----------|----------|--------------|----------|
| | Backend Full Translation | Error Keys Only (FE Mapping) | **Hybrid Key + Fallback** | Key + Args Only |
| Backend i18n Maintenance | ❌ High | ✅ None | ✅ Low | ✅ None |
| Frontend Knows Errors | ⚠️ No | ❌ Must Map | ✅ Via Key | ✅ Via Key |
| Graceful Fallback | N/A | ❌ No | ✅ Yes | ❌ No |
| API Response Size | ❌ Large | ✅ Small | ⚠️ Medium | ✅ Small |
| FR/pt-BR Support | ❌ Duplicate | ✅ Single Source | ✅ Single Source | ✅ Single Source |
| Implementation Complexity | ❌ High | ⚠️ Medium | ✅ Simple | ✅ Medium |
| Recommended | ❌ No | ⚠️ Maybe | ✅ **YES** | ⚠️ Maybe |

---

## Recommended Solution: Option 3 (Hybrid)

### Why Hybrid is BEST for Your Case

1. **You Already Have Frontend i18n**
   - Frontend handles FR/pt-BR translations
   - Backend should defer to frontend for all UI text
   - Backend only provides the ERROR CODE and DATA

2. **Graceful Degradation**
   - If frontend is missing a translation → shows English fallback
   - Users always see SOMETHING useful
   - No blank errors or crashes due to missing keys

3. **Incremental Adoption**
   - Start with English fallback working immediately
   - Gradually add FR/pt-BR translations to frontend
   - No rush to translate everything at once
   - No backend maintenance for translations

4. **Clean Separation of Concerns**
   - Backend: Identifies the problem (error code) + provides data (args)
   - Frontend: Communicates to user (translation) + handles UX
   - Each system owns its responsibility

5. **Future Proof**
   - Can add more languages without touching backend
   - Can reorganize frontend i18n structure without backend changes
   - Backend error codes remain stable

---

## Refactor Plan

### Phase 1: Update ApiError Record (IMMEDIATE)

**Current:**
```java
public record ApiError(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path
)
```

**Updated:**
```java
public record ApiError(
    Instant timestamp,
    int status,
    String error,
    String errorKey,           // NEW: frontend i18n key
    String message,            // Fallback English message
    Object[] messageArgs,      // NEW: arguments for parameterized messages
    String path
)
```

### Phase 2: Update ExceptionUtils.buildResponseEntity() (IMMEDIATE)

**Current:**
```java
public static ResponseEntity<ApiError> buildResponseEntity(
    HttpStatus status,
    ExceptionMessageEnum message,
    String path) {
    
    ApiError error = new ApiError(
        Instant.now(),
        status.value(),
        message.name(),
        message.getMessage(),
        path
    );
    return ResponseEntity.status(status).body(error);
}
```

**Updated:**
```java
public static ResponseEntity<ApiError> buildResponseEntity(
    HttpStatus status,
    ExceptionMessageEnum exceptionMessage,
    String path,
    Object... args) {
    
    String errorKey = toErrorKey(exceptionMessage.name());
    
    ApiError error = new ApiError(
        Instant.now(),
        status.value(),
        exceptionMessage.name(),
        errorKey,                          // NEW
        exceptionMessage.getMessage(),     // Fallback
        args.length > 0 ? args : null,    // NEW
        path
    );
    return ResponseEntity.status(status).body(error);
}

private static String toErrorKey(String enumName) {
    // Convert: AUTHENTICATION_REQUIRED → errors.auth.required
    return "errors." + enumName.toLowerCase()
        .replace("_", ".")
        .replaceAll("^errors\\.", "");  // Avoid double prefix
}
```

### Phase 3: Update GlobalExceptionHandler (IMMEDIATE)

```java
@ExceptionHandler(CustomEntityNotFoundException.class)
public ResponseEntity<ApiError> handleEntityNotFound(
    CustomEntityNotFoundException ex,
    HttpServletRequest request) {
    
    // Pass arguments if available
    return buildResponseEntity(
        HttpStatus.NOT_FOUND,
        ex.getExceptionEnum(),
        request.getRequestURI(),
        ex.getFormatArgs()  // if exception has parameterized values
    );
}
```

### Phase 4: Update CustomAuthenticationEntryPoint (IMMEDIATE)

```java
@Override
public void commence(HttpServletRequest request, HttpServletResponse response, 
    AuthenticationException ex) throws IOException, ServletException {
    
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ApiError error = new ApiError(
        Instant.now(),
        HttpStatus.UNAUTHORIZED.value(),
        ExceptionMessageEnum.AUTHENTICATION_REQUIRED.name(),
        "errors.auth.required",        // NEW: error key
        ExceptionMessageEnum.AUTHENTICATION_REQUIRED.getMessage(),
        null,                          // NEW: no args for this error
        request.getRequestURI()
    );

    response.getWriter().write(objectMapper.writeValueAsString(error));
}
```

### Phase 5: Delete messages.properties (CLEANUP)

- Remove `app/src/main/resources/messages.properties`
- Not needed with hybrid approach
- Avoids confusion with backend i18n

### Phase 6: Document Error Keys for Frontend (DOCUMENTATION)

**Create:** `.github/generated/ERROR_KEYS_REFERENCE.md`

**Content:**
```markdown
# Backend Error Keys Reference for Frontend i18n

| HTTP Status | Error Code | Error Key | Args | Example |
|-------------|-----------|-----------|------|---------|
| 401 | AUTHENTICATION_REQUIRED | `errors.auth.required` | None | "Authentication is required..." |
| 403 | ACCESS_DENIED | `errors.access.denied` | None | "You don't have permission..." |
| 403 | RECIPE_AUTHOR_ONLY | `errors.recipe.authorOnly` | None | "Only the recipe author..." |
| 404 | RECIPE_NOT_FOUND_BY_ID | `errors.recipe.notFoundById` | [recipeId] | "The recipe (id :: 123)..." |
| 404 | USER_NOT_FOUND | `errors.user.notFound` | [username] | "User 'john' not found." |

## Error Key Pattern

Backend error codes are converted to i18n keys using this pattern:

```
EnumName: RECIPE_NOT_FOUND_BY_ID
Error Key: errors.recipe.notFoundById (camelCase for clarity)
```

Frontend may use different structures, e.g.:
```json
{
  "errors": {
    "recipe": {
      "notFoundById": "A receita (id :: {0}) não foi encontrada."
    }
  }
}
```
```

### Phase 7: Frontend Integration Guide (DOCUMENTATION)

**Create:** `.github/generated/FRONTEND_ERROR_HANDLING.md`

**Content:**
```markdown
# Frontend Error Handling Integration Guide

## Response Format

```json
{
  "timestamp": "2026-06-02T...",
  "status": 401,
  "error": "AUTHENTICATION_REQUIRED",
  "errorKey": "errors.auth.required",
  "message": "Authentication is required to access this resource.",
  "messageArgs": null,
  "path": "/hmr/api/recipes"
}
```

## Implementation Example

```typescript
import { useTranslation } from 'react-i18next';

const handleApiError = (error) => {
  const { t } = useTranslation();
  
  // Preferred: Use errorKey + messageArgs if available
  if (error.response?.data?.errorKey) {
    const key = error.response.data.errorKey;
    const args = error.response.data.messageArgs || [];
    const message = t(key, { defaultValue: error.response.data.message, ...args });
    // Show message to user
    return message;
  }
  
  // Fallback: Use English message from backend
  return error.response?.data?.message || 'An error occurred';
};
```

## Expected i18n File Structure

```json
// en.json
{
  "errors": {
    "auth": {
      "required": "Authentication is required to access this resource."
    },
    "recipe": {
      "notFoundById": "The recipe (id :: {{0}}) couldn't be found."
    }
  }
}

// pt-BR.json
{
  "errors": {
    "auth": {
      "required": "Autenticação é necessária para acessar este recurso."
    },
    "recipe": {
      "notFoundById": "A receita (id :: {{0}}) não foi encontrada."
    }
  }
}
```

## Mapping Between Backend Codes and Frontend Keys

| Backend | Frontend Key | Translation Needed |
|---------|--------------|-------------------|
| AUTHENTICATION_REQUIRED | errors.auth.required | EN (fallback), FR, pt-BR |
| RECIPE_NOT_FOUND_BY_ID | errors.recipe.notFoundById | EN (fallback), FR, pt-BR |
| USER_NOT_FOUND | errors.user.notFound | EN (fallback), FR, pt-BR |
```

---

## Implementation Timeline

| Phase | Task | Duration | Priority |
|-------|------|----------|----------|
| 1 | Update `ApiError` record | 15 min | 🔴 HIGH |
| 2 | Update `ExceptionUtils` | 20 min | 🔴 HIGH |
| 3 | Update `GlobalExceptionHandler` | 15 min | 🔴 HIGH |
| 4 | Update `CustomAuthenticationEntryPoint` | 15 min | 🔴 HIGH |
| 5 | Delete `messages.properties` | 5 min | 🟡 MEDIUM |
| 6 | Create ERROR_KEYS_REFERENCE.md | 20 min | 🟢 LOW |
| 7 | Create FRONTEND_ERROR_HANDLING.md | 30 min | 🟢 LOW |
| **TOTAL** | **Hybrid i18n Implementation** | **~2 hours** | |

---

## Migration Path from Current Design

### Current State (After Step 3)
- ❌ `messages.properties` exists (unused i18n)
- ✅ `ExceptionMessageEnum` has error messages
- ✅ `ApiError` record in place
- ❌ No error keys for frontend

### Target State (After This Refactor)
- ✅ `messages.properties` deleted
- ✅ `ExceptionMessageEnum` remains (for error messages)
- ✅ `ApiError` updated with `errorKey` and `messageArgs`
- ✅ Error keys available for frontend translation
- ✅ Documentation for frontend developers

### Zero Breaking Changes
- Existing clients still get `error`, `message`, `status`
- New fields `errorKey` and `messageArgs` are optional additions
- Frontend can adopt at its own pace
- English fallback ensures compatibility

---

## Why NOT messages.properties?

The `messages.properties` file creates a false sense of i18n support:

1. **It's Unused**
   - Not injected into error handlers
   - Spring MessageSource not configured
   - Just sitting in resources directory

2. **It Creates Duplication**
   - Same messages exist in frontend i18n files
   - Two places to maintain translations
   - Inconsistency source

3. **It Conflicts with Frontend i18n**
   - Frontend already handles FR/pt-BR
   - Backend shouldn't duplicate that work
   - Violates separation of concerns

4. **It's Not Scalable**
   - Adding new language requires backend changes
   - Backend must maintain ALL languages
   - Frontend i18n system is better for this

**Solution:** Delete it and use the hybrid error key + fallback approach instead.

---

## Error Response Examples

### After Implementation

**Unauthenticated Request:**
```json
{
  "timestamp": "2026-06-02T10:30:15.456789Z",
  "status": 401,
  "error": "AUTHENTICATION_REQUIRED",
  "errorKey": "errors.auth.required",
  "message": "Authentication is required to access this resource.",
  "messageArgs": null,
  "path": "/hmr/api/recipes"
}
```

**Recipe Not Found (with parameter):**
```json
{
  "timestamp": "2026-06-02T10:30:15.456789Z",
  "status": 404,
  "error": "RECIPE_NOT_FOUND_BY_ID",
  "errorKey": "errors.recipe.notFoundById",
  "message": "The recipe (id :: 999) couldn't be found.",
  "messageArgs": ["999"],
  "path": "/hmr/api/recipes/999"
}
```

**Frontend Translation (pt-BR):**
```
Input: Backend response with errorKey="errors.recipe.notFoundById", 
       messageArgs=["999"]

Frontend looks up: i18n.t("errors.recipe.notFoundById", { 
  defaultValue: message,
  args: ["999"]
})

Output to user: "A receita (id :: 999) não foi encontrada."
```

---

## Conclusion

### Summary Table

| Approach | Messages.properties | Hybrid Error Keys |
|----------|-------------------|------------------|
| Backend Maintenance | ❌ Must maintain all languages | ✅ English only + error codes |
| Frontend Ownership | ❌ Shared responsibility | ✅ Own all translations |
| Scalability | ❌ Hard to add languages | ✅ Easy to add languages |
| Fallback Support | ❌ No fallback | ✅ English fallback |
| i18n Duplication | ❌ Duplicates frontend | ✅ Leverages frontend |
| Recommended | ❌ NO | ✅ **YES** |

**Recommendation:** Implement the **Hybrid Error Keys + Fallback approach** and delete `messages.properties`.

This approach:
- Leverages your existing frontend i18n (FR/pt-BR)
- Keeps backend responsibility minimal (error codes only)
- Provides graceful fallback (English messages)
- Scales easily to new languages
- Maintains clean separation of concerns

---

## Start Next Phase?

When ready, implement the refactoring plan above to move from Step 3 (current duplicate i18n) to a sustainable hybrid approach that coordinates with your frontend.

**Should I proceed with implementing this refactor?**

# Most recent refactor plan:
## Refactor Plan — Add `translationKey` and placeholders to `ApiError` (Single-solution)

Checklist (apply these changes)
- Add new fields to `ApiError` (preferred: `messageParams: Map<String,Object>`; alternate: `messagePlaceholders: List<String>` + `messageArgs: List<Object>`).
- Update the error-builder helper (e.g. `ExceptionUtils.buildResponseEntity(...)`) to populate `translationKey` and param fields.
- Update `GlobalExceptionHandler` and all places that construct `ApiError` (including security entry points) to pass params/placeholders when available.
- Update API docs and generated `.github` docs (`ERROR_KEYS_REFERENCE.md`, `FRONTEND_ERROR_HANDLING.md`) with the new contract and examples.
- Add unit/integration tests asserting the JSON shape and param interpolation behavior.
- Coordinate with frontend (Angular) developers to consume `translationKey` and `messageParams` and to fall back to `message` if translation missing.

Goal
- Send a stable i18n key from the backend together with an English fallback message for logs and structured parameters for interpolation.
- Keep the change non-breaking: new fields are optional/nullable.

Design choices
- Field name: `translationKey` (per your preference). Equivalent to `errorKey`.
- Placeholder strategy:
    - Recommended: `messageParams: Map<String,Object>` (named parameters) — easiest & least error-prone for frontend.
    - Alternative (minimal): `messagePlaceholders: List<String>` + `messageArgs: List<Object>` (ordered lists).
- Preserve existing fields (`status`, `error`, `message`, `path`) for backwards compatibility.

API contract (recommended)
```json
{
  "timestamp": "2026-06-02T10:30:15.456789Z",
  "status": 403,
  "error": "USER_NOT_AUTHORIZED",
  "translationKey": "exception.userNotAuthorized",
  "message": "User Maxime cannot access this feature.",
  "messageParams": { "username": "Maxime" },
  "path": "/hmr/api/some-action"
}