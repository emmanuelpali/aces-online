# PacesOnline Development Handoff

> Copy this file for each handoff.  
> Recommended filename:
>
> ```text
> docs/handoffs/YYYY-MM-DD-issue-N-short-description.md
> ```

## 1. Handoff Metadata

```text
Date:
Prepared by:
Project: PacesOnline
Milestone:
Active issue:
Issue URL:
Current checkpoint:
Repository:
Branch:
Pull request:
Exact head commit SHA:
Last known good commit SHA:
Working tree status:
```

## 2. Read These Sources First

Read these in order before giving implementation advice:

1. `PROJECT_CONTEXT.md`
2. `docs/current-milestone.md`
3. The active GitHub issue
4. Relevant files under `docs/decisions/`
5. This handoff
6. The exact branch and head commit listed above

Repository documents are more authoritative than remembered chat context.

## 3. Project Summary

PacesOnline is a running journal application and portfolio project.

Core workflow:

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

Target Version 1 release:

```text
September 30, 2026
```

Adjust this section only when the stable project summary changes.

## 4. Current Objective

Describe the active issue in one paragraph.

```text
[What is being built now, why it is needed, and what outcome marks completion.]
```

## 5. Current Scope

### In scope

- [Requirement 1]
- [Requirement 2]
- [Requirement 3]

### Out of scope

- [Explicit exclusion 1]
- [Explicit exclusion 2]
- [Explicit exclusion 3]

Do not implement out-of-scope work even when it is part of a future milestone.

## 6. Current Checkpoint

```text
Checkpoint name:
Checkpoint objective:
Checkpoint entry conditions:
Checkpoint exit conditions:
```

### Work required now

- [Task 1]
- [Task 2]
- [Task 3]

### Evidence required before moving on

- [Code or diff]
- [Command output]
- [Test result]
- [Exact commit SHA]
- [Explanation or decision record]

Do not advance to the next checkpoint until the exit conditions are satisfied.

## 7. Work Completed Since the Previous Handoff

- [Completed change]
- [Completed test]
- [Completed documentation]
- [Decision made]

Reference exact commits where possible:

```text
<short SHA> <commit message>
```

## 8. Current Repository State

### Relevant files

```text
[path/to/file]
[path/to/file]
[path/to/file]
```

### Files added

```text
[path]
```

### Files modified

```text
[path]
```

### Files removed or renamed

```text
[old path] -> [new path]
```

### Uncommitted work

```text
[None, or describe exactly what remains uncommitted.]
```

## 9. Acceptance-Criteria Status

Use the active issue’s actual acceptance criteria.

- [ ] Criterion not started
- [~] Criterion in progress
- [x] Criterion completed and verified

For completed criteria, include evidence:

```text
Criterion:
Evidence:
Commit:
Command/test:
```

## 10. Commands Run and Results

### Build

```powershell
[command]
```

Result:

```text
[BUILD SUCCESS, failure output, or not yet run]
```

### Tests

```powershell
[command]
```

Result:

```text
[test summary]
```

### Application startup

```powershell
[command]
```

Result:

```text
[startup result]
```

### Manual verification

```powershell
[command]
```

Result:

```text
[response]
```

Do not claim a command passed unless its output is included or independently verified.

## 11. Current Configuration and Environment

```text
Java version:
Spring Boot version:
Active Spring profile:
Required environment variables:
Local dependencies:
Docker required:
Database required:
Other tools required:
```

Never include real passwords, signing keys, tokens, private keys, or production credentials.

## 12. Locked Architecture Decisions Relevant to This Issue

Copy only the decisions that matter for the active issue.

Examples:

- The frontend communicates only with the BFF.
- The BFF uses generated OpenAPI clients to call internal services.
- Business logic and repositories are not generated.
- Each service owns its own database.
- Kubernetes supplies runtime values through ConfigMaps, Secrets, and environment variables.
- One application image is built and configured per environment.
- RabbitMQ, not Kafka, is used for Version 1 asynchronous work.

These decisions remain locked unless changed through an explicit architectural decision record.

## 13. Decisions Made During This Issue

Record decisions that should survive the current conversation.

### Decision: [Short title]

```text
Context:
Decision:
Reason:
Alternatives considered:
Consequences:
Needs ADR: Yes/No
```

Repeat this section for each meaningful decision.

## 14. Open Questions

- [Question]
- [Question]

For each question, state whether it blocks current work.

```text
Question:
Blocking: Yes/No
Owner:
Needed by:
```

## 15. Known Problems or Risks

- [Problem or risk]
- [Temporary workaround]
- [Follow-up issue needed]

Do not hide failures or unresolved uncertainty.

## 16. Review Status

```text
Latest review verdict:
Commit reviewed:
Blocking findings:
Optional improvements:
Items fixed after review:
Items still open:
```

### Exact-commit review rule

For every GitHub review:

1. Provide the PR URL.
2. Provide the exact head commit SHA.
3. State which SHA is being reviewed.
4. Verify the commit and changed files directly.
5. Treat screenshots and local outputs as local evidence.
6. Do not infer that local work was not pushed merely because a GitHub view appears stale.
7. When sources conflict, report the conflict and request or verify the current SHA.

## 17. Next Action

State exactly one next action.

```text
Next action:
Why this is next:
Expected evidence:
Stop condition:
```

Avoid broad instructions such as “continue the issue.”

## 18. Suggested First Message for the New Chat

Copy and fill in this prompt:

```text
I am continuing the PacesOnline project.

Please use the attached or pasted handoff as the current session context.

Before advising:
1. Read PROJECT_CONTEXT.md.
2. Read docs/current-milestone.md.
3. Read the active GitHub issue.
4. Use the exact branch and commit SHA in the handoff.
5. Treat repository documents as more authoritative than remembered chat context.
6. Stay within the active issue.
7. Work through one checkpoint at a time.
8. Do not implement the entire issue for me.
9. Explain the concept before asking me to code.
10. Review my code, tests, command output, and exact Git commit.

Begin with the “Next Action” section only. Do not move to a later checkpoint until I provide the required evidence.
```

## 19. End-of-Session Update Checklist

Before ending the current session:

- [ ] Update completed work.
- [ ] Update the exact head commit SHA.
- [ ] Update acceptance-criteria status.
- [ ] Record commands and results.
- [ ] Record decisions.
- [ ] Record blockers and open questions.
- [ ] Set exactly one next action.
- [ ] Confirm no secrets were copied into the handoff.
- [ ] Commit the handoff when it contains durable project information.
