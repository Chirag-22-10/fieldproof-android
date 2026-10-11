from fastapi import FastAPI

app = FastAPI(
    title="FieldProof API",
    version="0.1.0",
    description="Starter API skeleton for the FieldProof portfolio demo. Persistence and auth are not implemented yet.",
)


@app.get("/health", tags=["health"])
def health() -> dict[str, str]:
    """Return API process health only; does not verify database or storage connectivity."""
    return {"status": "ok", "service": "fieldproof-api", "stage": "starter"}
