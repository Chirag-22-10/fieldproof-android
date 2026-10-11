# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project aims to follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Offline-first inspection vertical slice:
  - Room data layer for inspections, checklist items, evidence and sync operations.
  - Repositories (`InspectionRepository`, `SyncRepository`, `RemoteRepository`)
    and `InspectionViewModel`.
  - Jetpack Compose UI for the inspection list/detail, checklist and evidence
    capture, plus a CameraX capture screen with runtime permission handling.
  - Retrofit/OkHttp client matching the documented `/api/v1` contract,
    including the `Idempotency-Key` header on evidence uploads.
- `fieldproof-starter/`: project brief, ADRs, API contract, data model, sync
  state machine, FastAPI health skeleton and synthetic fixtures.
- Project documentation: README, CONTRIBUTING, CODE_OF_CONDUCT, SECURITY and
  this changelog; Apache-2.0 license and NOTICE.
- GitHub Actions CI for the Android module and the backend skeleton.

### Fixed

- Apply the Hilt Gradle plugin and add the Hilt dependencies so dependency
  injection is actually wired at runtime.
- Bump Hilt to 2.60.1 for AGP 9 compatibility.
- Annotate `MainActivity` with `@AndroidEntryPoint` so `hiltViewModel()` resolves.
- Add `INTERNET` and `ACCESS_NETWORK_STATE` permissions.
- Disable the default WorkManager initializer for on-demand configuration.
- Replace the deprecated `LocalLifecycleOwner` import with the
  `lifecycle-runtime-compose` equivalent.

## [0.0.1] - 2026-10-10

### Added

- Initial project scaffold and product brief.
