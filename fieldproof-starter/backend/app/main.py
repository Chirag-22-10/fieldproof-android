from datetime import datetime, timezone
from typing import Dict
from uuid import UUID

from fastapi import FastAPI, Header, HTTPException
from pydantic import BaseModel, Field


app = FastAPI(
    title="FieldProof API",
    version="0.1.0",
)


# ---------------------------------------------------------------------------
# Demo in-memory storage
# ---------------------------------------------------------------------------
#
# We are intentionally starting with in-memory storage so we can prove the
# API contract first. PostgreSQL will replace this storage layer afterward.
#

inspections: Dict[str, dict] = {}
idempotency_results: Dict[str, dict] = {}


# ---------------------------------------------------------------------------
# Models
# ---------------------------------------------------------------------------

class HealthResponse(BaseModel):
    status: str
    service: str
    stage: str


class CreateInspectionRequest(BaseModel):
    id: UUID
    title: str = Field(min_length=1, max_length=200)
    location: str = Field(min_length=1, max_length=200)
    createdAt: int


class CreateInspectionResponse(BaseModel):
    id: UUID
    status: str


# ---------------------------------------------------------------------------
# Health
# ---------------------------------------------------------------------------

@app.get("/health", response_model=HealthResponse)
async def health():
    return HealthResponse(
        status="ok",
        service="fieldproof-api",
        stage="starter",
    )


# ---------------------------------------------------------------------------
# Create inspection
# ---------------------------------------------------------------------------

@app.post(
    "/api/v1/inspections",
    response_model=CreateInspectionResponse,
)
async def create_inspection(
    request: CreateInspectionRequest,
    idempotency_key: str | None = Header(
        default=None,
        alias="Idempotency-Key",
    ),
):
    if not idempotency_key:
        raise HTTPException(
            status_code=400,
            detail="Idempotency-Key header is required",
        )

    # Same request repeated -> return the original result.
    if idempotency_key in idempotency_results:
        return idempotency_results[idempotency_key]

    inspection_id = str(request.id)

    # Client-generated UUID gives us deterministic identity.
    if inspection_id in inspections:
        existing = inspections[inspection_id]

        result = CreateInspectionResponse(
            id=UUID(existing["id"]),
            status=existing["status"],
        )

        idempotency_results[idempotency_key] = result
        return result

    now = datetime.now(timezone.utc)

    inspection = {
        "id": inspection_id,
        "title": request.title,
        "location": request.location,
        "status": "DRAFT",
        "createdAt": request.createdAt,
        "serverCreatedAt": now.isoformat(),
    }

    inspections[inspection_id] = inspection

    result = CreateInspectionResponse(
        id=request.id,
        status="DRAFT",
    )

    idempotency_results[idempotency_key] = result

    return result


# ---------------------------------------------------------------------------
# Get inspection
# ---------------------------------------------------------------------------

@app.get("/api/v1/inspections/{inspection_id}")
async def get_inspection(inspection_id: UUID):
    inspection = inspections.get(str(inspection_id))

    if inspection is None:
        raise HTTPException(
            status_code=404,
            detail="Inspection not found",
        )

    return inspection