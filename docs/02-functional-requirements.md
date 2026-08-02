# PacesOnline Functional Requirements

## Document Status

* **Project:** AcesOnline
* **Version:** 1.0 Draft
* **Current section:** Identity and Access
* **Target release:** September 30, 2026

## Priority Definitions

* **MUST:** Required for the September portfolio release.
* **SHOULD:** Implement after the core workflow is stable.
* **LATER:** Explicitly excluded from Version 1.

---

# 1. Identity and Access

## 1.1 User Roles

PacesOnline will initially support three roles:

### Student

A student can:

* Browse published courses.
* Enroll in courses.
* Access enrolled course content.
* Complete lessons.
* Take quizzes.
* View personal quiz results and progress.

### Instructor

An instructor can:

* Perform all permitted student actions.
* Create and update courses.
* Create lessons and quizzes.
* Publish eligible courses.
* View results for students enrolled in their courses.

### Administrator

An administrator can:

* View and manage user accounts.
* Assign or remove instructor privileges.
* Disable and re-enable user accounts.
* Moderate published courses.
* Perform administrative operations across the platform.

---

## IAM-001: User Registration

**Priority:** MUST

A visitor must be able to create a student account using an email address and password.

### Acceptance Criteria

* Registration requires an email address and password.
* The email address is normalized before it is stored.
* Email addresses must be unique, ignoring letter case.
* Invalid email addresses are rejected.
* The password must meet the configured password policy.
* Passwords are never stored in plain text.
* Passwords are hashed using a secure password encoder.
* Newly registered users receive the `STUDENT` role.
* Newly registered accounts are enabled by default.
* The response must not include the password or password hash.
* Duplicate email registration returns a clear conflict response.
* Validation errors use the platform’s standard error format.
* Passwords and authentication credentials must never appear in application logs.
* When asynchronous messaging is introduced, successful registration must create a `UserRegistered` event for downstream processing.

---

## IAM-002: User Login

**Priority:** MUST

A registered user must be able to authenticate using their email address and password.

### Acceptance Criteria

* Valid credentials produce an access token and refresh token.
* Invalid credentials return a generic authentication error.
* The response must not reveal whether an email address exists.
* Disabled accounts cannot log in.
* The access token identifies the user and their assigned roles.
* Tokens must not contain passwords or unnecessary personal information.
* Authentication failures are recorded without logging credentials.
* Access tokens expire after a configurable duration.
* Refresh tokens expire after a configurable duration.
* Token durations must be supplied through externalized configuration.

---

## IAM-003: Authenticated User Profile

**Priority:** MUST

An authenticated user must be able to retrieve their own basic account information.

### Acceptance Criteria

* The endpoint requires a valid access token.
* The response includes the user ID, email, roles, account status, and account creation date.
* The response does not expose password information or refresh tokens.
* A user can only retrieve their own profile through this endpoint.
* Expired, malformed, or invalid tokens are rejected consistently.

---

## IAM-004: Access-Token Authorization

**Priority:** MUST

Protected operations must only be available to authenticated users with the required permissions.

### Acceptance Criteria

* Requests without a valid access token are rejected.
* Expired access tokens are rejected.
* Users without the required role receive a forbidden response.
* Authentication failures and authorization failures return different status codes.
* Role requirements are enforced on the server, not only in the React interface.
* Internal services must not rely exclusively on hidden frontend controls for authorization.
* Authorization rules are covered by automated tests.

---

## IAM-005: Token Refresh

**Priority:** MUST

An authenticated session must be renewable without requiring the user to enter their credentials whenever an access token expires.

### Acceptance Criteria

* A valid refresh token can be exchanged for a new access token.
* Refresh tokens are stored securely.
* Refresh tokens are not stored as plain text where avoidable.
* An expired refresh token cannot be used.
* A revoked refresh token cannot be used.
* Refresh-token rotation is supported.
* Using a rotated token invalidates the previous refresh token.
* Refresh-token expiration is configurable.
* Invalid refresh attempts return a generic authentication error.

---

## IAM-006: Logout

**Priority:** MUST

An authenticated user must be able to end their current session.

### Acceptance Criteria

* Logging out revokes the refresh token for the current session.
* A revoked refresh token cannot produce new access tokens.
* Already-issued access tokens remain valid only until their configured expiration.
* Logging out one session does not automatically terminate every user session.
* A later administrative capability may revoke all sessions for a user.

---

## IAM-007: Role Management

**Priority:** MUST

An administrator must be able to assign or remove the instructor role.

### Acceptance Criteria

* Only administrators can change another user’s roles.
* A student can be promoted to instructor.
* An instructor can be returned to student-only access.
* Duplicate roles are not stored.
* Role changes take effect for newly issued access tokens.
* Unauthorized role-management attempts are rejected.
* Role changes are recorded as security audit events.
* The system prevents accidental removal of required administrative access where that would leave the platform without an administrator.

---

## IAM-008: Account Disabling

**Priority:** MUST

An administrator must be able to disable or re-enable a user account.

### Acceptance Criteria

* Only administrators can disable or re-enable accounts.
* A disabled user cannot log in.
* A disabled user cannot refresh an authentication session.
* Existing refresh tokens for a disabled account are revoked or rejected.
* Protected services reject requests for disabled users when account status validation is required.
* Re-enabling an account does not restore previously revoked refresh tokens.
* Account-status changes are recorded as security audit events.

---

## IAM-009: Security Audit Events

**Priority:** MUST

Important identity and access operations must produce security audit records.

### Events to Record

* Successful registration.
* Successful login.
* Failed login.
* Logout.
* Refresh-token rejection.
* Role assignment.
* Role removal.
* Account disabling.
* Account re-enabling.

### Acceptance Criteria

* Audit entries include an event type and timestamp.
* Audit entries include the affected user when known.
* Administrative events include the administrator who performed the action.
* Passwords, raw tokens, and other credentials are never included.
* Audit recording must not expose sensitive information to the client.
* Failure to write a non-critical audit entry must be visible through logs and metrics.

---

## IAM-010: Email Verification

**Priority:** SHOULD

A new user should eventually verify ownership of their email address.

### Acceptance Criteria

* Registration can create an unverified account.
* A verification token is time-limited.
* Verification tokens can only be used once.
* A verified email status is recorded.
* The verification workflow can use the Notification Service.
* Version 1 may allow unverified users to explore the platform while restricting selected actions.

---

## IAM-011: Password Reset

**Priority:** SHOULD

A user who has forgotten their password should be able to request a password reset.

### Acceptance Criteria

* The request response does not reveal whether the email exists.
* Reset tokens are time-limited and single-use.
* Completing a reset invalidates the reset token.
* Existing refresh tokens may be revoked after a successful password change.
* The new password must satisfy the current password policy.
* Password-reset notifications are processed through the Notification Service.

---

## IAM-012: Social Authentication

**Priority:** LATER

Authentication through Google, Microsoft, GitHub, or another external identity provider is outside the Version 1 scope.

---

## IAM-013: Multi-Factor Authentication

**Priority:** LATER

Multi-factor authentication is outside the Version 1 scope.

---

# 2. Identity Non-Functional Requirements

## Security

* Passwords must use a configurable secure hashing algorithm.
* Credentials and raw tokens must never be logged.
* Secrets must not be committed to Git.
* Authentication configuration must be externalized.
* Production secrets must be supplied using Kubernetes Secrets or an equivalent secret-management mechanism.
* Error responses must not reveal internal stack trPaces.
* Login endpoints must support protection against repeated automated attempts.
* All production communication must use HTTPS.

## Configuration

The Identity Service must demonstrate:

* `application.yml`
* Environment-specific profiles.
* `@ConfigurationProperties`
* Environment-variable overrides.
* Validation of required configuration.
* Secure handling of signing keys and database credentials.
* Different token lifetimes for local, test, and production environments where appropriate.

## Reliability

* Database changes must use versioned migrations.
* Registration must not create partially initialized users.
* Duplicate concurrent registration attempts must be handled safely.
* Token revocation data must survive application restarts.
* Identity operations must produce useful logs and metrics.

## Testing

The Identity Service must include:

* Unit tests for business rules.
* Repository integration tests.
* Controller or API tests.
* Spring Security authorization tests.
* Authentication failure tests.
* Refresh-token rotation tests.
* Account-disabling tests.
* PostgreSQL integration tests using Testcontainers.
* Tests confirming sensitive fields are not returned.

## Standard Error Responses

Identity endpoints must use a consistent error structure containing:

* Timestamp.
* HTTP status.
* Stable error code.
* Human-readable message.
* Request or correlation identifier.
* Field-level validation errors where applicable.

Internal stack traces must not be returned to clients.

---

# 3. Identity Definition of Done

The Identity and Access milestone is complete when:

1. A visitor can register as a student.
2. A registered user can log in.
3. The user receives working access and refresh tokens.
4. The user can retrieve their own profile.
5. Refresh-token rotation works.
6. Logout revokes the current refresh token.
7. Student, instructor, and administrator authorization rules work.
8. An administrator can manage instructor privileges.
9. An administrator can disable and re-enable accounts.
10. Important security events are audited.
11. Database migrations run automatically.
12. Configuration is externalized and validated.
13. Automated tests cover the critical workflows.
14. The service runs locally through Docker.
15. The API is documented through OpenAPI.
16. The implementation and architectural decisions are documented.
