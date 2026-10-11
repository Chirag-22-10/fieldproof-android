# Contributing to FieldProof

Thanks for your interest in improving FieldProof! This document explains how to
propose changes and what we expect from a pull request.

By participating you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md).

## Ways to contribute

- **Report a bug** — use the [bug report template](.github/ISSUE_TEMPLATE/bug_report.yml).
- **Request a feature** — use the [feature request template](.github/ISSUE_TEMPLATE/feature_request.yml).
- **Ask a question** — open a discussion or a question issue.
- **Send a pull request** — see the workflow below.

For anything security-related, follow [SECURITY.md](SECURITY.md) instead of
opening a public issue.

## Development setup

1. **JDK 17** and the **Android SDK (API 37)** must be installed.
2. Create `local.properties` at the repo root (git-ignored):
   ```properties
   sdk.dir=/absolute/path/to/Android/sdk
   ```
3. Build:
   ```bash
   ./gradlew :app:assembleDebug
   ```

Optional backend:

```bash
cd fieldproof-starter
python -m venv .venv && source .venv/bin/activate
pip install -r backend/requirements.txt
pytest
```

## Workflow

1. **Fork** the repository and create a topic branch off `main`:
   ```bash
   git switch -c feat/short-description
   ```
2. Make focused changes. Keep unrelated refactors in separate pull requests.
3. **Run the checks** before pushing:
   ```bash
   ./gradlew :app:assembleDebug :app:testDebugUnitTest
   ```
4. Commit with a clear message (see below) and push to your fork.
5. Open a pull request against `main` and fill in the template.

## Commit messages

Use the imperative mood and a short summary line. Conventional-commit style
prefixes are encouraged but not required:

```text
feat(sync): enqueue evidence uploads with backoff
fix(manifest): add INTERNET permission
docs: document the sync state machine
```

## Coding guidelines

- **Kotlin / Compose** first; match the existing style and module layout.
- Keep the **data layer** (Room DAOs, repositories) free of Android UI concerns.
- Prefer **immutable state** and unidirectional data flow in the UI layer.
- Add or update **tests** for behaviour you change.
- Do **not** introduce secrets, API keys, real client data or real evidence.
  Use synthetic fixtures only.

## Pull request checklist

- [ ] Focused on a single concern.
- [ ] Builds locally (`./gradlew :app:assembleDebug`).
- [ ] Unit tests pass (`./gradlew :app:testDebugUnitTest`).
- [ ] Documentation updated where behaviour changed.
- [ ] No secrets or private data included.

## Licensing of contributions

This project is licensed under the [Apache License, Version 2.0](LICENSE).
Unless you explicitly state otherwise, any contribution you intentionally submit
for inclusion is provided under the same license, as described in Section 5 of
the license (inbound = outbound).

## Attribution

Reuse of third-party code must respect its license. Add attribution to
[`NOTICE`](NOTICE) and keep dependency licenses compatible with Apache-2.0.
