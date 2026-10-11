package com.spacedrift.fieldproof.data.remote

data class HealthResponse(
    val status: String,
    val service: String,
    val stage: String
)

data class CreateInspectionRequest(
    val id: String,
    val title: String,
    val location: String,
    val createdAt: Long
)

data class CreateInspectionResponse(
    val id: String,
    val status: String
)

data class UploadEvidenceResponse(
    val id: String,
    val status: String
)