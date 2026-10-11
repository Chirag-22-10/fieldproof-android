<div align="center">

# FieldProof Android

**Offline-first field inspection & evidence capture for Android.**

Capture inspections, photos and notes with no connectivity — then sync reliably and export a verifiable report.

[![CI](https://github.com/spacedrift-in/fieldproof-android/actions/workflows/ci.yml/badge.svg)](https://github.com/spacedrift-in/fieldproof-android/actions/workflows/ci.yml)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android%2024%2B-brightgreen.svg)](#prerequisites)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

<sub>A [Spacedrift](https://spacedrift.in) portfolio demonstration · synthetic data only</sub>

</div>

---

Field evidence gets scattered across photos and chat messages, making reports
slow and difficult to verify. FieldProof explores one credible solution:
an app that keeps working in airplane mode, makes sync state visible, and never
silently loses evidence.

> **Status:** early, active development. The offline vertical slice (inspection →
> checklist → evidence capture) is implemented and builds for both debug and
> release. Sync, report export and conflict resolution are **not implemented yet**.
> There are no performance or compliance claims in this repository.

## Table of contents

- [Why FieldProof](#why-fieldproof)
- [Features](#features)
- [Architecture](#architecture)
- [Tech stack](#tech-stack)
- [Project structure](#project-structure)
- [Getting started](#getting-started)
- [Testing](#testing)
- [Backend skeleton](#backend-skeleton)
- [Roadmap](#roadmap)
- [Design documents](#design-documents)
- [Contributing](#contributing)
- [Security](#security)
- [License](#license)
- [Contact](#contact)

## Why FieldProof

Inspection teams, facilities operators and field service businesses lose time
reconciling evidence gathered offline. The interesting engineering problems are
not the screens — they are:

- **Durability.** An inspection and its evidence must survive process death
  while offline.
- **Exactly-once *effect*.** WorkManager retries are at-least-once; uploads must
  be idempotent so a retry never creates duplicate evidence.
- **Honest conflict handling.** Concurrent edits are surfaced, never silently
  overwritten (optimistic concurrency with server versions).

This project is a deliberate exploration of those problems on Android, backed by
a documented API contract and a small FastAPI skeleton. See
[`fieldproof-starter/docs/`](fieldproof-starter/docs/) for the full brief.

## Features

| Area | What's implemented |
| :--- | :--- |
| **Inspections** | Create inspections offline; list and reopen them after process death (Room as source of truth) |
| **Checklists** | Default checklist items with required/optional flags; per-item completion tracked locally |
| **Evidence** | Timestamped photo capture (CameraX) and text notes, stored app-privately and linked to an inspection |
| **Sync model** | Durable local `SyncOperation` records with a stable idempotency key per evidence upload |
| **Permissions** | Runtime camera permission with an actionable denied state |
| **UI** | Jetpack Compose, Material 3, loading / empty / error-aware states |
| **API contract** | Retrofit client matching the documented `/api/v1` endpoints, incl. `Idempotency-Key` |

**Not implemented yet:** background upload execution, retry/backoff wiring,
conflict-resolution UI, report export, authentication.

## Architecture

MVVM with repositories over an offline-first data layer. Room is the single
source of truth on-device; the network is an eventual destination, never the
read path.

```mermaid
flowchart TD
    UI["Compose UI<br/>(MainActivity, Camera)">
    VM["InspectionViewModel"]
    Repo["Repositories<br/>Inspection · Sync · Remote"]
    Room["Room database<br/>inspections · checklist_items · evidence · sync_operations"]
    WM["WorkManager<br/>SyncWorker (HiltWorkerFactory)"]
    API["FieldProofApi (Retrofit/OkHttp)"]
    Server["FastAPI sync service"]

    UI --> VM --> Repo
    Repo --> Room
    Repo --> WM
    WM --> API
    API -. "not wired yet" .-> Server
    Room -. "Flow" .-> UI
```

**Evidence sync state machine** (see
[`sync-state-machine.md`](fieldproof-starter/docs/sync-state-machine.md)):

```text
queued ─▶ uploading ─▶ synced
             │
             ├─▶ queued             (transient failure / retry scheduled)
             ├─▶ failed             (retry budget exhausted / permanent error)
             └─▶ needs_resolution   (version conflict — requires user input)
```

## Tech stack

| Layer | Choice |
| :--- | :--- |
| Language | Kotlin 2.2 |
| UI | Jetpack Compose + Material 3 |
| DI | Dagger Hilt (KSP) |
| Persistence | Room (KSP) |
| Async | Kotlin Coroutines + Flow |
| Background | WorkManager (Hilt-integrated) |
| Camera | CameraX |
| Networking | Retrofit + OkHttp (+ Gson) |
| Build | Gradle 9.6, AGP 9.4.1, Java 17 |
| Backend (skeleton) | FastAPI, Uvicorn (Python 3.11+) |

Exact versions are pinned in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## Project structure

```text
FieldProof/
├── app/                         # Android application module
│   └── src/main/java/com/spacedrift/fieldproof/
│       ├── data/
│       │   ├── local/           # Room entities, DAOs, database
│       │   ├── remote/          # Retrofit API + DTOs
│       │   └── repository/      # Inspection / Sync / Remote repositories
│       ├── di/                  # Hilt modules (App, Network)
│       ├── sync/                # SyncWorker (WorkManager)
│       └── ui/
│           ├── camera/          # CameraX capture screen
│           └── inspection/      # InspectionViewModel
├── fieldproof-starter/          # Brief, ADRs, API contract, FastAPI skeleton
├── gradle/libs.versions.toml    # Version catalog
├── CONTRIBUTING.md
├── SECURITY.md
└── LICENSE
```

## Getting started

### Prerequisites

| Tool | Version |
| :--- | :--- |
| JDK | 17 |
| Android SDK Platform | API 37 (`compileSdk`/`targetSdk` 37, `minSdk` 24) |
| Gradle | via wrapper (`./gradlew`, Gradle 9.6) |
| Python (backend, optional) | 3.11+ |

Point Gradle at your SDK by creating `local.properties` (git-ignored):

```properties
sdk.dir=/absolute/path/to/Android/sdk
```

### Build & run

```bash
# Assemble debug APK
./gradlew :app:assembleDebug

# Install on a connected device/emulator
./gradlew :app:installDebug
```

Open the folder in Android Studio to use the Run configuration.

### Release build

```bash
./gradlew :app:assembleRelease   # runs R8 minification + lint vital
```

## Testing

```bash
./gradlew :app:testDebugUnitTest      # JVM unit tests
./gradlew :app:connectedDebugAndroidTest  # instrumented tests (device/emulator)
```

## Backend skeleton

The starter includes a FastAPI health endpoint. Only `GET /health` exists today.

```bash
cd fieldproof-starter
python -m venv .venv && source .venv/bin/activate
pip install -r backend/requirements.txt
uvicorn backend.app.main:app --reload
```

The Android client points at `http://10.0.2.2:8000/` (the host loopback as seen
from the Android emulator). The full API contract — including idempotent evidence
upload and optimistic concurrency — is in
[`fieldproof-starter/docs/api-contract.md`](fieldproof-starter/docs/api-contract.md).

## Roadmap

| Milestone | Required evidence | State |
| :--- | :--- | :--- |
| 1 · Definition | User flow, data model, API contract, acceptance criteria | ✅ docs in `fieldproof-starter/docs/` |
| 2 · Vertical slice | One complete offline workflow on synthetic fixtures | 🟡 capture done; sync pending |
| 3 · Reliability | Failure recovery, access checks, automated tests | ⬜ |
| 4 · Demonstration | Repeatable demo, screenshots, documented limitations | ⬜ |
| 5 · Handoff | Reproducible setup, build instructions, maintenance notes | 🟡 this README |

## Design documents

- [Product brief](fieldproof-starter/README.md)
- [Architecture decision (ADR 0001)](fieldproof-starter/docs/decisions/0001-architecture.md)
- [API contract](fieldproof-starter/docs/api-contract.md)
- [Data model](fieldproof-starter/docs/data-model.md)
- [Sync state machine](fieldproof-starter/docs/sync-state-machine.md)
- [Acceptance criteria](fieldproof-starter/docs/acceptance-criteria.md)
- [Demo plan](fieldproof-starter/docs/demo.md)

## Contributing

Contributions are welcome. Please read [`CONTRIBUTING.md`](CONTRIBUTING.md) and
the [`CODE_OF_CONDUCT.md`](CODE_OF_CONDUCT.md) first. In short: fork, branch,
keep changes focused, run the checks below, and open a pull request.

```bash
./gradlew :app:assembleDebug :app:testDebugUnitTest
```

## Security

Please do **not** open public issues for vulnerabilities. See
[`SECURITY.md`](SECURITY.md) for the reporting process. Secrets, private client
data and real evidence must never be committed — this repository uses synthetic
fixtures only.

## License

Licensed under the [Apache License, Version 2.0](LICENSE). See [`NOTICE`](NOTICE)
for attribution.

## Contact

[Spacedrift](https://spacedrift.in) · [Project enquiries](mailto:spacedrift.contact@gmail.com)

Copyright © 2026 Spacedrift.
