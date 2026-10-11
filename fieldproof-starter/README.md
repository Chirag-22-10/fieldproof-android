# FieldProof Android (starter)

**Status:** Planning + backend skeleton. This is not a complete or deployable product yet.

FieldProof is an offline-first inspection demo built with synthetic data. The Android application is planned in Kotlin/Jetpack Compose; the API is planned in FastAPI with PostgreSQL.

## Repository layout

- `android/` — Android app to be created with Android Studio after toolchain versions are agreed.
- `backend/` — minimal FastAPI health endpoint and tests.
- `docs/decisions/` — architecture decisions.
- `docs/` — data model, API contract, sync behavior, acceptance criteria, demo plan.
- `tests/fixtures/` — synthetic fixtures only.

## Backend quick start

Use Python 3.11 or newer in a virtual environment. From the repository root:

```bash
python -m venv .venv
source .venv/bin/activate  # Windows: .venv\\Scripts\\activate
pip install -r backend/requirements.txt
uvicorn app.main:app --app-dir backend --reload
```

Open `http://127.0.0.1:8000/health` to check the API and `http://127.0.0.1:8000/docs` for the interactive API docs.

Run backend tests from the repository root after installing requirements:

```bash
pytest backend/tests
```

These commands are for the included minimal backend skeleton only. Android build commands will be added after the Android project and toolchain are created and verified.

## Safety and data handling

- Use synthetic data only in fixtures and screenshots.
- Do not commit secrets, private client information, real inspection photos, or access tokens.
- Server-side authorization and database persistence are not implemented in this skeleton.
