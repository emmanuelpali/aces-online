# PacesOnline Project Context

## Purpose of This File

This file is the durable source of truth for the PacesOnline project.

Use it to preserve project-wide context across:

- New ChatGPT conversations
- GitHub issues and pull requests
- Development milestones
- Architecture discussions
- Code reviews
- Future tooling or coding assistants

This file contains stable project decisions. The active GitHub issue contains the detailed scope for the current piece of work.

## Project Summary

**PacesOnline** is a running journal application where users can:

- Register and log in
- Record completed runs
- Optionally attach one photo to a run
- Review and filter run history
- View weekly and monthly running statistics

PacesOnline is primarily a portfolio and learning project designed to demonstrate intermediate-level engineering with Java, Spring Boot, React, distributed systems, testing, Docker, Kubernetes, and production-readiness practices.

## Target Release

The target for a portfolio-ready Version 1 is:

**September 30, 2026**

Quality is more important than feature count.

The release must demonstrate one complete, secure, tested, documented, and deployable workflow.

## Developer Goals

The project should help the developer:

- Become competitive for intermediate Java and Spring Boot roles
- Deepen Spring Boot knowledge
- Learn Spring Boot configuration and production practices
- Refresh React and TypeScript
- Practice microservice design
- Learn OpenAPI contract-driven development
- Practice asynchronous messaging
- Prepare for the CKAD certification
- Build a strong portfolio project for technical interviews
- Explain architectural choices and tradeoffs confidently

## Core User Workflow

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

## Version 1 Features

### Identity and Access

A user can:

- Register with an email address and password
- Log in
- Receive access and refresh tokens
- Refresh an authenticated session
- Log out
- View their profile
- Be prevented from accessing protected resources when disabled

Initial roles:

```text
USER
ADMIN
```

### Run Management

A user can:

- Create a run
- View one of their runs
- Update one of their runs
- Delete one of their runs
- View their run history
- Filter runs by date range
- Filter runs by run type
- Add optional notes
- Attach one optional photo
- View weekly statistics
- View monthly statistics

A user must never be able to view or modify another user’s private runs.

### Run Fields

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

Supported run types for Version 1:

```text
EASY
RECOVERY
LONG
TEMPO
INTERVAL
RACE
```

Average pace must be calculated by the backend from distance and duration. The client must not be trusted to supply the final pace.

## Planned Architecture

```text
                       React
                         |
                  Spring Boot BFF
                         |
             -------------------------
             |                       |
      Identity Service          Run Service
             |                       |
       PostgreSQL               PostgreSQL
                                     |
                            S3-compatible storage
                                     |
                                 RabbitMQ
                                     |
                          Notification Worker
```

The project uses a **monorepo**, but each application is an independent build and deployment unit.

## Application Responsibilities

### React Frontend

Responsible for:

- Registration and login screens
- Run creation and editing forms
- Run-history screens
- Weekly and monthly dashboard views
- Client-side validation and user feedback

The frontend communicates with the BFF and must not directly call internal services.

### Spring Boot BFF

Responsible for:

- Providing a frontend-oriented API
- Calling internal services through generated OpenAPI clients
- Hiding internal service locations
- Aggregating responses when necessary
- Propagating authentication context safely
- Translating internal failures into consistent frontend-facing responses

The BFF must remain thin. Domain business rules belong in the service that owns the domain.

### Identity Service

Responsible for:

- User registration
- Password hashing
- Authentication
- Access tokens
- Refresh tokens
- Logout and token revocation
- Roles
- Account status
- Basic security auditing

The Identity Service owns its own PostgreSQL data.

### Run Service

Responsible for:

- Run creation
- Run updates
- Run deletion
- Run retrieval
- Run-history filtering
- Pace calculation
- Weekly and monthly statistics
- Photo metadata
- Publishing relevant domain events

The Run Service owns its own PostgreSQL data and must not query the Identity Service database.

### Notification Worker

Responsible for consuming RabbitMQ messages.

Potential events:

```text
UserRegistered
RunRecorded
WeeklySummaryRequested
```

Potential responsibilities:

- Log notification activity
- Send a welcome email
- Send a weekly running summary
- Record failed notification attempts

Mailpit may be used locally to capture email without sending real messages.

## Locked Architecture Decisions

These decisions remain active even when they are outside the current issue:

- The project is named **PacesOnline**.
- The repository may still be named `aces-online` until it is intentionally renamed.
- The project uses a monorepo.
- Each Spring Boot service is independently buildable and deployable.
- The frontend communicates only with the BFF.
- The BFF communicates with internal services.
- Identity Service and Run Service publish OpenAPI contracts.
- OpenAPI Generator will generate Java clients used by the BFF.
- Generated clients may use Spring `RestClient`, `WebClient`, or another deliberately selected supported library.
- Service business logic, repositories, persistence entities, and domain implementations will not be generated.
- Generated server interfaces may be considered later if the benefit justifies the additional complexity.
- Each service owns its own data.
- Services must not query another service’s database.
- PostgreSQL is the source of truth.
- RabbitMQ is used for asynchronous work.
- Kafka is not part of Version 1.
- MinIO is used for local S3-compatible photo storage.
- Photos are not stored as large binary values in PostgreSQL.
- Redis is optional and may only be introduced after the core workflow is stable.
- Elasticsearch is not part of Version 1.
- Kubernetes deployment uses one container image per application with externally supplied runtime configuration.
- Kubernetes ConfigMaps and Secrets supply configuration; they do not replace Spring Boot’s configuration model.

## Data and Infrastructure Decisions

### PostgreSQL

- PostgreSQL is the source of truth for application data.
- Each service owns its own database or logical schema.
- Services must not directly query another service’s tables.

### RabbitMQ

RabbitMQ is selected because Version 1 needs:

- Queue-based asynchronous processing
- Routing
- Retries
- Dead-letter handling
- Background notification work

Kafka should only be reconsidered for future requirements involving high-throughput streams, durable replay, or many independent historical consumers.

### Redis

Redis is a stretch goal for:

- Weekly statistics caching
- Monthly statistics caching
- Recent-run summaries

Redis must not become the source of truth.

### Photo Storage

Use:

- MinIO for local development
- An S3-compatible API
- PostgreSQL for object keys and metadata

Version 1 supports one optional photo per run.

## API and OpenAPI Decisions

- The frontend communicates through the BFF.
- The BFF communicates with backend services.
- OpenAPI contracts document service APIs.
- OpenAPI Generator generates Java clients for the BFF.
- Generated clients replace scattered handwritten HTTP plumbing in the BFF.
- API DTOs remain separate from JPA entities.
- Services do not share persistence entities.
- API errors follow a consistent structure.
- APIs are treated as contracts.
- Full server business implementations are written manually.
- OpenAPI server-interface generation may be evaluated later, but is not currently required.

Planned contract locations:

```text
contracts/
├── identity-api/
│   └── openapi.yml
└── run-api/
    └── openapi.yml
```

## Repository Structure

```text
paces-online/
├── PROJECT_CONTEXT.md
├── README.md
├── frontend/
├── bff/
├── services/
│   ├── identity-service/
│   ├── run-service/
│   └── notification-worker/
├── contracts/
│   ├── identity-api/
│   └── run-api/
├── infrastructure/
│   ├── docker/
│   └── kubernetes/
└── docs/
    ├── 01-project-vision.md
    ├── 02-functional-requirements.md
    ├── 03-high-level-architecture.md
    ├── roadmap.md
    ├── current-milestone.md
    ├── engineering-journal.md
    ├── HANDOFF_TEMPLATE.md
    └── decisions/
```

Directories may be added when the milestone that needs them begins. Empty placeholder directories are not required.

## Delivery Roadmap

### Milestone 0 — Project Discovery

Deliverables:

- Project vision
- Functional requirements
- Architecture
- Roadmap
- Project context
- Initial architectural decision records

### Milestone 1 — Identity and Access

Deliverables:

- Identity Service bootstrap
- Spring profiles
- Type-safe configuration properties
- PostgreSQL
- Flyway migrations
- Registration
- Login
- Access tokens
- Refresh tokens
- Logout
- Authenticated profile
- Security tests
- Dockerfile
- OpenAPI documentation

### Milestone 2 — Run Management

Deliverables:

- Run data model
- Create, view, update, and delete run
- Run history
- Filtering
- Pace calculation
- Validation
- Database migrations
- Automated tests

### Milestone 3 — BFF and React

Deliverables:

- OpenAPI contracts
- Generated Java clients
- BFF integration
- Registration and login screens
- Run form
- Run-history screen
- Authentication handling
- Consistent error handling

### Milestone 4 — Photos and Messaging

Deliverables:

- Photo upload
- MinIO integration
- RabbitMQ
- Domain events
- Notification Worker
- Retries
- Dead-letter handling
- Local email capture

### Milestone 5 — Dashboard and Quality

Deliverables:

- Weekly statistics
- Monthly statistics
- Redis caching only if justified
- Docker Compose
- Testcontainers integration
- End-to-end workflow testing

### Milestone 6 — Kubernetes Release

Deliverables:

- Kubernetes manifests
- Deployments
- Services
- Ingress
- ConfigMaps
- Secrets
- Health probes
- Resource controls
- CI/CD
- Metrics
- Documentation
- Screenshots
- Demo workflow

## Version 1 Scope Discipline

The following are required before stretch features:

1. Registration and login work.
2. Run CRUD works.
3. Authorization prevents cross-user access.
4. Run history and filtering work.
5. Core integration tests pass.
6. Docker Compose runs the system.
7. The application is deployable to Kubernetes.

Optional features must not delay testing, deployment, documentation, or release preparation.

## Explicitly Out of Scope for Version 1

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

## Development Process

Every feature follows this sequence:

1. Define the requirement.
2. Write acceptance criteria.
3. Design the API.
4. Design the data model.
5. Implement the feature.
6. Add automated tests.
7. Review and refactor.
8. Update documentation.

Every major technology must solve a defined problem.

## Quality Standards

A feature is not complete merely because it runs.

Each completed feature should include:

- Clear acceptance criteria
- Validation
- Consistent error handling
- Security considerations
- Automated tests
- Externalized configuration
- Useful logs
- Updated documentation
- Clean naming and package organization

## Current Status

### Completed

**Issue #1 — Bootstrap Identity Service**

Completed outcomes:

- Standalone Spring Boot Identity Service
- Spring Boot Actuator
- Health endpoint
- Liveness and readiness groups
- Application-context startup test
- Service README
- Successful local `clean verify`
- Successful health response with status `UP`

### Active

**Issue #2 — Configure Spring Profiles and Type-Safe Configuration**

The active issue establishes:

- `application.yml`
- `application-local.yml`
- `application-test.yml`
- `application-prod.yml`
- External profile activation
- `@ConfigurationProperties`
- Configuration validation
- Fail-fast startup
- Environment-variable overrides
- Safe secret handling
- Configuration tests
- Updated documentation

JWT implementation, authentication, JPA, Flyway, and real PostgreSQL connectivity remain out of scope for Issue #2.

## Source-of-Truth Reading Order

At the beginning of a new development session:

1. Read `PROJECT_CONTEXT.md`.
2. Read `docs/current-milestone.md`.
3. Read the active GitHub issue.
4. Read relevant architectural decision records.
5. Read the latest handoff created from `docs/HANDOFF_TEMPLATE.md`.
6. Confirm the current branch and exact head commit.
7. Identify the next unfinished acceptance criterion.
8. Do not expand the active issue’s scope.

When sources disagree:

1. The latest approved architectural decision record wins for that decision.
2. `PROJECT_CONTEXT.md` wins for stable project-wide context.
3. The active GitHub issue wins for current implementation scope.
4. The latest exact commit wins for code state.
5. Chat memory is advisory only and must not override repository sources.

## Pull Request Review Protocol

For every PR review:

- Provide the PR URL.
- Provide the exact head commit SHA.
- Review that exact commit, not an indexed or cached summary.
- State the SHA being reviewed.
- Distinguish between:
  - Code visible at the exact commit
  - Local command output supplied by the developer
  - Behavior not independently executed
- If GitHub views disagree, report the inconsistency instead of assuming the developer failed to push.
- Re-check the exact head SHA after every update.

## Mentoring Approach

The assistant should act as a senior engineer and mentor.

The assistant should:

- Explain concepts before implementation where necessary
- Work through one checkpoint at a time
- Review architectural and coding decisions
- Challenge unnecessary complexity
- Identify overengineering
- Keep development within the active issue
- Review code, tests, commands, and exact commits
- Explain tradeoffs
- Help prepare interview explanations
- Avoid implementing the entire project on the developer’s behalf
