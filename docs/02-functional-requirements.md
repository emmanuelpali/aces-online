# PacesOnline Functional Requirements

## Document Status

- **Project:** PacesOnline
- **Document:** Functional Requirements
- **Target release:** Version 1
- **Target date:** September 30, 2026
- **Status:** Draft for implementation
- **Current milestone:** Milestone 1 — Identity and Access

## 1. Purpose

PacesOnline is a running journal application that allows users to record completed runs, optionally attach a photo, review their run history, and view weekly or monthly running statistics.

Version 1 focuses on delivering one complete, secure, tested, documented, and deployable workflow:

```text
Register or log in
        ↓
Record a completed run
        ↓
Optionally attach a photo
        ↓
View and filter run history
        ↓
Review weekly or monthly statistics
```

Quality, security, and deployability are more important than adding a large number of features.

## 2. User Roles

### 2.1 USER

A user can:

- Register with an email address and password.
- Log in and manage an authenticated session.
- View their own profile.
- Create, view, update, and delete their own runs.
- View and filter their run history.
- Attach one optional photo to a run.
- View weekly and monthly statistics for their own runs.

A user must not be able to access or modify another user’s private data.

### 2.2 ADMIN

An administrator can:

- View basic user-account information needed for support or administration.
- Disable or enable user accounts.
- Access administrative functionality protected by the `ADMIN` role.

Administrative functionality must not expose password hashes, refresh tokens, credentials, or internal secrets.

## 3. Identity and Access Requirements

### 3.1 Registration

The system must allow a new user to register using:

- Email address
- Password

The system must:

- Require a valid email address.
- Require a password that satisfies the configured password policy.
- Prevent registration with an email address that is already in use.
- Store passwords only as secure password hashes.
- Assign the `USER` role by default.
- Record the account creation time.
- Return a consistent success or error response.

The system must not return password hashes or sensitive security data in API responses.

### 3.2 Login

The system must allow a registered and enabled user to log in using their email address and password.

On successful login, the system must:

- Authenticate the user.
- Return an access token.
- Return a refresh token.
- Include token-expiration information where appropriate.
- Record relevant security audit information.

On failed login, the system must:

- Return a consistent authentication error.
- Avoid revealing whether a specific email address exists.
- Avoid logging raw passwords or tokens.

A disabled user must not be allowed to log in.

### 3.3 Access Tokens

Access tokens must:

- Represent the authenticated user.
- Include the user identifier.
- Include the user’s assigned roles or authorities.
- Have a configurable expiration time.
- Be validated before protected resources are accessed.
- Be rejected when expired, malformed, or invalid.

### 3.4 Refresh Tokens

The system must allow a valid refresh token to be exchanged for a new authenticated session.

Refresh tokens must:

- Have a configurable expiration time.
- Be revocable.
- Be rejected when expired, invalid, or revoked.
- Be stored and handled securely.
- Not be exposed in logs.

### 3.5 Logout

The system must allow an authenticated user to log out.

Logout must:

- Revoke or invalidate the relevant refresh token or session.
- Prevent the revoked refresh token from being reused.
- Return a consistent response whether the token has already been revoked or is no longer valid.

### 3.6 Authenticated Profile

An authenticated user must be able to view their profile.

The profile response may include:

- User ID
- Email address
- Assigned roles
- Account status
- Creation time

The profile response must not include:

- Password hash
- Raw password
- Refresh token
- Internal security metadata not intended for the user

### 3.7 Account Status

A user account must have an enabled or disabled status.

When an account is disabled:

- New logins must be rejected.
- Protected requests must not be authorized.
- Refresh-token operations must be rejected.
- Existing credentials must stop providing access according to the selected token-revocation strategy.

## 4. Run Management Requirements

### 4.1 Run Data

A run may contain:

```text
id
userId
startedAt
runType
distanceKilometres
durationSeconds
averagePace
perceivedEffort
notes
photoKey
createdAt
updatedAt
```

Supported run types for Version 1 are:

```text
EASY
RECOVERY
LONG
TEMPO
INTERVAL
RACE
```

### 4.2 Create a Run

An authenticated user must be able to record a completed run.

Required fields:

- Start date and time
- Run type
- Distance in kilometres
- Duration in seconds

Optional fields:

- Perceived effort
- Notes
- One photo

The system must:

- Associate the run with the authenticated user.
- Calculate average pace from distance and duration.
- Ignore or reject any client-supplied value that attempts to override the calculated pace.
- Record creation and update timestamps.
- Validate all submitted values.
- Return the created run.

### 4.3 View a Run

An authenticated user must be able to view one of their own runs by ID.

The system must:

- Return the run when it belongs to the authenticated user.
- Reject access when the run belongs to another user.
- Return a consistent not-found response when the run does not exist or is not accessible.
- Avoid revealing private information about another user’s run.

### 4.4 Update a Run

An authenticated user must be able to update one of their own runs.

The user may update:

- Start date and time
- Run type
- Distance
- Duration
- Perceived effort
- Notes
- Optional photo reference

The system must:

- Verify ownership before applying the update.
- Recalculate average pace when distance or duration changes.
- Validate the updated data.
- Update the modification timestamp.
- Reject attempts to update another user’s run.

### 4.5 Delete a Run

An authenticated user must be able to delete one of their own runs.

The system must:

- Verify ownership before deletion.
- Reject attempts to delete another user’s run.
- Handle associated photo metadata according to the photo-storage design.
- Return a consistent response when the run does not exist or is not accessible.

### 4.6 Run History

An authenticated user must be able to view their run history.

The response must:

- Include only the authenticated user’s runs.
- Support pagination.
- Use a stable and documented sort order.
- Show the most recent runs first by default.

### 4.7 Run Filtering

The user must be able to filter run history by:

- Start date
- End date
- Run type

Filters may be combined.

The system must:

- Validate date ranges.
- Reject an end date earlier than the start date.
- Return an empty result when no runs match.
- Apply filtering only to the authenticated user’s data.

## 5. Average Pace Requirements

Average pace must be calculated by the backend from distance and duration.

The system must:

- Reject zero or negative distance.
- Reject zero or negative duration.
- Use a consistent unit and representation for pace.
- Document the pace format in the API contract.
- Recalculate pace after relevant updates.
- Avoid trusting a pace value supplied by the client.

## 6. Perceived Effort Requirements

A user may assign a perceived-effort value to a run.

The allowed range must be documented and validated consistently.

For Version 1, the recommended scale is:

```text
1 to 10
```

The field may be omitted.

## 7. Notes Requirements

A user may add notes to a run.

The system must:

- Allow notes to be omitted.
- Enforce a configured maximum length.
- Treat notes as plain text.
- Prevent notes from causing unsafe rendering in the frontend.

## 8. Photo Requirements

A user may attach one optional photo to a run.

The system must:

- Store the photo in S3-compatible object storage.
- Use MinIO for local development.
- Store only photo metadata and the object key in PostgreSQL.
- Associate the photo with the correct user and run.
- Prevent one user from accessing another user’s private photo.
- Validate supported file types and maximum file size.
- Reject invalid or unsupported uploads.
- Handle replacement or deletion consistently.

Large binary photo content must not be stored directly in PostgreSQL.

## 9. Statistics Requirements

### 9.1 Weekly Statistics

An authenticated user must be able to view statistics for a selected week.

Weekly statistics should include:

- Number of runs
- Total distance
- Total duration
- Average pace where meaningful

### 9.2 Monthly Statistics

An authenticated user must be able to view statistics for a selected month.

Monthly statistics should include:

- Number of runs
- Total distance
- Total duration
- Average pace where meaningful

### 9.3 Statistics Rules

The system must:

- Include only the authenticated user’s runs.
- Define week and month boundaries consistently.
- Document timezone handling.
- Return zero-valued statistics when no runs exist.
- Calculate statistics from PostgreSQL as the source of truth.
- Treat Redis, if introduced later, only as a cache.

## 10. Authorization and Privacy Requirements

All protected functionality must require authentication.

The system must:

- Identify the authenticated user from validated authentication data.
- Enforce ownership checks in the service that owns the data.
- Prevent users from reading or modifying another user’s runs.
- Prevent users from accessing another user’s photos.
- Restrict administrative operations to authorized administrators.
- Avoid relying only on frontend checks for authorization.
- Avoid accepting a client-supplied `userId` as proof of ownership.

The Run Service must not query the Identity Service database directly.

Each service must own and control access to its own data.

## 11. API Requirements

The frontend must communicate through the Spring Boot BFF.

The BFF must:

- Provide a frontend-oriented API.
- Call internal services using generated OpenAPI clients.
- Hide internal service locations.
- Propagate authentication context safely.
- Translate internal errors into consistent frontend-facing errors.
- Remain thin and avoid owning domain business rules.

Internal services must:

- Publish and maintain OpenAPI contracts.
- Keep API DTOs separate from JPA entities.
- Validate incoming requests.
- Return consistent error responses.
- Avoid exposing internal stack traces.
- Avoid exposing secrets or sensitive configuration.
- Treat APIs as contracts.

## 12. Validation Requirements

The system must validate all externally supplied data.

Validation failures must:

- Return a consistent error structure.
- Identify invalid fields where appropriate.
- Use clear, user-safe messages.
- Avoid exposing internal implementation details.

Examples include:

- Invalid email address
- Weak or missing password
- Duplicate email address
- Unsupported run type
- Zero or negative distance
- Zero or negative duration
- Invalid perceived-effort value
- Notes exceeding the maximum length
- Invalid date range
- Unsupported photo type
- Oversized photo upload

## 13. Error-Handling Requirements

API errors must use a consistent structure.

An error response should contain fields such as:

```text
timestamp
status
code
message
path
correlationId
fieldErrors
```

The exact contract must be defined in the relevant OpenAPI specification.

Error responses must:

- Use appropriate HTTP status codes.
- Avoid exposing stack traces.
- Avoid exposing SQL, database details, secrets, tokens, or internal hostnames.
- Include enough information for the client to handle the failure.
- Include a correlation identifier where available.

## 14. Audit and Logging Requirements

The system must log important operational and security events, including:

- Successful and failed login attempts
- User registration
- Account disablement or enablement
- Token revocation
- Run creation, update, and deletion
- Photo upload failures
- Message-consumption failures

Logs must:

- Avoid raw passwords.
- Avoid full access or refresh tokens.
- Avoid unnecessary sensitive personal data.
- Use structured logging where practical.
- Include correlation identifiers where practical.

## 15. Messaging Requirements

RabbitMQ will be used for asynchronous work and event delivery.

Potential Version 1 events include:

```text
UserRegistered
RunRecorded
WeeklySummaryRequested
```

The Notification Worker may:

- Log notification activity.
- Send a welcome email.
- Send a weekly running summary.
- Record failed notification attempts.

Messaging must support:

- Retry handling
- Dead-letter handling
- Idempotent consumers
- Failure logging

RabbitMQ is not required for Issue #1 and must not be introduced before the relevant milestone.

## 16. Service Health Requirements

Each Spring Boot service must provide operational health information using Spring Boot Actuator.

The Identity Service must expose:

```text
GET /actuator/health
```

A healthy service must return HTTP `200` with:

```json
{
	"status": "UP"
}
```

Health endpoints must later support Kubernetes startup, readiness, and liveness probes.

## 17. Testing Requirements

Each completed feature must include appropriate automated tests.

The project should use:

- JUnit 5
- Mockito
- `@WebMvcTest`
- `@DataJpaTest`
- `@SpringBootTest`
- Testcontainers
- Security tests
- Integration tests
- Contract-oriented API tests

At minimum:

- Application-context startup must be tested.
- Validation behavior must be tested.
- Authorization boundaries must be tested.
- Users must be prevented from accessing other users’ runs.
- Persistence behavior must be tested.
- Core service integrations must be tested.

Testcontainers should be introduced when a real external dependency such as PostgreSQL is added, not during the initial application bootstrap.

## 18. Configuration Requirements

Application configuration must be externalized.

The project will use:

- `application.yml`
- Environment-specific profiles
- Environment-variable overrides
- `@ConfigurationProperties`
- Configuration validation
- Fail-fast startup for invalid required configuration

Secrets must not be committed to source control.

Environment-specific values must include:

- Database connection
- Token durations
- Signing configuration
- RabbitMQ connection
- Object-storage connection
- Allowed origins
- File-size limits

Detailed profile and configuration work belongs to Issue #2.

## 19. Deployment Requirements

Version 1 must be deployable using:

- Docker
- Docker Compose
- Kubernetes

Kubernetes deployment must eventually include:

- Deployments
- Services
- Ingress
- ConfigMaps
- Secrets
- Startup probes
- Readiness probes
- Liveness probes
- Resource requests
- Resource limits
- Rolling updates
- Rollback support

The core workflow must work through Docker Compose before the Kubernetes release is considered complete.

## 20. Version 1 Completion Criteria

Version 1 is not complete until:

1. Registration and login work.
2. Access and refresh tokens work.
3. Disabled users are prevented from accessing protected resources.
4. Run CRUD works.
5. Authorization prevents cross-user access.
6. Run history and filtering work.
7. Average pace is calculated by the backend.
8. Optional photo upload works.
9. Weekly and monthly statistics work.
10. Core automated tests pass.
11. Docker Compose runs the system.
12. The application is deployable to Kubernetes.
13. APIs and local-development workflows are documented.
14. The complete user workflow can be demonstrated.

## 21. Explicitly Out of Scope for Version 1

Version 1 does not include:

- Elasticsearch
- Kafka
- GPS tracking
- Route maps
- Wearable-device integrations
- Social feeds
- Followers
- Likes
- Comments
- Running clubs
- Challenges
- Training plans
- Payments
- Mobile applications
- AI coaching
- Advanced analytics
- Service mesh
- Event sourcing

These features must not delay the secure, tested, documented, and deployable Version 1 workflow.

## 22. Current Issue Boundary

The current issue is:

```text
Issue #1 — Bootstrap Identity Service
```

Issue #1 includes only:

- Generate the Spring Boot application.
- Establish the initial package structure.
- Add Spring Boot Actuator.
- Expose a health endpoint.
- Add an application-context startup test.
- Document how to run the service locally.
- Confirm the application builds and starts.

Issue #1 does not include:

- User entities
- Registration
- Authentication
- JWT
- Refresh tokens
- Flyway migrations
- RabbitMQ
- Redis
- Photo storage
- Business logic
