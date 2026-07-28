# Project Vision

## Working Name

**LearnForge**

This is a temporary project name and may be changed later.

## Product Vision

LearnForge is a cloud-native learning management platform that allows instructors to create structured courses, publish study materials and assess students through quizzes.

Students can discover courses, enroll, complete lessons, take quizzes and monitor their learning progress.

The first version will focus on a small but complete learning workflow rather than attempting to reproduce every feature found in large learning-management platforms.

## Project Purpose

The project has two connected purposes.

### Product purpose

Provide a functional platform where instructors can publish educational content and students can study and complete assessments.

### Engineering purpose

Demonstrate the ability to design, build, test, deploy and operate a distributed application using modern Java, Spring Boot and Kubernetes practices.

The project will also serve as a structured environment for learning Spring Boot in greater depth and preparing for the Certified Kubernetes Application Developer certification.

## Target Users

### Student

A student can:

* Create an account.
* Sign in securely.
* Browse published courses.
* Enroll in a course.
* Read course lessons.
* Take available quizzes.
* View quiz scores.
* Track course progress.

### Instructor

An instructor can:

* Create a course.
* Edit course information.
* Create and organize lessons.
* Create multiple-choice quizzes.
* Publish or unpublish courses.
* View student quiz submissions and results.

### Administrator

An administrator can:

* View and manage users.
* assign or remove instructor privileges.
* Review published courses.
* Disable inappropriate or invalid content.

## Version 1 Scope

Version 1 will include:

* User registration and authentication.
* Student, instructor and administrator roles.
* Course creation and publishing.
* Text-based lessons.
* Course enrollment.
* Multiple-choice quizzes.
* Automatic quiz grading.
* Student progress tracking.
* Email or simulated notifications.
* Basic administrative management.

## Initial Product Decisions

The first version will use the following rules:

* Courses will be free.
* Students can enroll immediately without instructor approval.
* Quizzes will initially contain automatically graded multiple-choice questions.
* Each quiz question will have one or more configured correct answers.
* Manual grading may be introduced later for written-answer questions.
* Course materials will initially use text or Markdown rather than video streaming.

These decisions keep the first version manageable without preventing future expansion.

## Out of Scope for Version 1

The following features will not be included initially:

* Payments and subscriptions.
* Video hosting or streaming.
* Live virtual classrooms.
* Student-to-student chat.
* Discussion forums.
* Mobile applications.
* AI-generated lessons or tutoring.
* Advanced certificates.
* Marketplace functionality.
* Real-time collaborative editing.
* Complex manually graded examinations.

## Technical Objectives

The project should demonstrate:

* Spring Boot application design.
* Spring configuration and externalized configuration.
* REST API design.
* OpenAPI contract-first development.
* Generated Java API clients.
* A Backend for Frontend layer.
* Service-level database ownership.
* PostgreSQL persistence.
* Spring Security and token-based authentication.
* RabbitMQ asynchronous messaging.
* Docker containerization.
* Local orchestration with Docker Compose.
* Kubernetes deployments and services.
* ConfigMaps and Secrets.
* Readiness, liveness and startup probes.
* Resource requests and limits.
* Horizontal scaling.
* Spring Boot Actuator.
* Metrics and monitoring.
* Automated testing.
* CI/CD with GitHub Actions.
* Architecture documentation and decision records.
- Redis caching and cache invalidation
- Elasticsearch full-text course search
- Eventual consistency between PostgreSQL and Elasticsearch
- RabbitMQ retries and dead-letter queues

## Success Criteria

The project will be considered successful when:

1. A student can register, enroll in a course, complete lessons and submit a quiz.
2. An instructor can create and publish a course containing lessons and quizzes.
3. Quiz submissions are graded correctly and recorded.
4. Important actions generate asynchronous events for notifications or auditing.
5. The frontend communicates through the BFF rather than calling internal services directly.
6. REST clients are generated from OpenAPI contracts.
7. The entire system can run locally using Docker Compose.
8. The application can be deployed to Kubernetes.
9. Kubernetes deployments include configuration, health probes and resource controls.
10. The project contains meaningful automated tests.
11. Application health and metrics can be observed.
12. The repository clearly explains the architecture, decisions, setup and deployment process.
- Published course data can be cached through Redis.
- Students can search published courses through Elasticsearch.
- PostgreSQL remains the source of truth.
- Course publishing events update the search index asynchronously.

## Guiding Principles

* Business requirements should justify technical choices.
* Every service owns its own data.
* APIs are treated as contracts.
* Complexity is introduced gradually.
* Technologies are added only when they solve a defined problem.
* Testing, security and observability are part of implementation, not final decorations.
* Documentation must explain both the decisions made and the alternatives considered.
