# ADR 0001: Offline-first Android with a sync API

- **Status:** Proposed; confirm during Milestone 1 review
- **Date:** 2026-10-10

## Context

Field inspections must continue without network access, preserve photos and notes across process restarts, and synchronize reliably after reconnecting. The client demonstration must include failure recovery and must not make unsupported performance or compliance claims.

## Decision

- Android client: Kotlin, Jetpack Compose, Room, WorkManager, CameraX.
- App structure: MVVM with repositories and a small domain layer for inspection rules.
- API: FastAPI, with PostgreSQL as the server-side source of truth for shared records.
- Evidence files: app-private local storage before upload; server storage behind an adapter.
- Reports: generated server-side from an immutable/versioned reviewed snapshot.
- IDs: client-generated UUIDs for offline-created records.
- Sync: durable local operations with stable idempotency keys; server uniqueness constraints; optimistic concurrency/version checks.

## Consequences

- The app can read and write local inspection state while offline.
- WorkManager provides durable scheduling and retry opportunities, not an exactly-once delivery guarantee.
- The API must implement idempotency and conflict handling explicitly.
- Local database records and files need recovery handling if one write succeeds and the other fails.
- The server must validate required evidence and permissions; client checks are for usability only.

## Alternatives considered

- **Online-only app:** rejected because offline capture is a core requirement.
- **Firebase-first backend:** possible alternative, but FastAPI/PostgreSQL is preferred for explicit API contracts, relational constraints, and a portfolio-friendly backend.
- **Client-generated PDF:** deferred because server-side generation from a reviewed snapshot gives a consistent report artifact.

## Open questions

1. Which authentication mechanism is needed for the demo (simple seeded demo users or a real identity provider)?
2. Is local filesystem storage sufficient for the demo server, or is object storage required from the start?
3. Are reports stored permanently, or regenerated from the same immutable snapshot?
4. What maximum photo size and supported image formats should be accepted?
