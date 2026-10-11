package com.spacedrift.fieldproof.data.repository

import com.spacedrift.fieldproof.data.remote.CreateInspectionRequest
import com.spacedrift.fieldproof.data.remote.FieldProofApi
import com.spacedrift.fieldproof.data.remote.HealthResponse
import com.spacedrift.fieldproof.data.remote.UploadEvidenceResponse
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val api: FieldProofApi
) {

    suspend fun health(): HealthResponse {
        return api.health()
    }

    suspend fun createInspection(
        id: String,
        title: String,
        location: String,
        createdAt: Long,
        idempotencyKey: String
    ) = api.createInspection(
        request = CreateInspectionRequest(
            id = id,
            title = title,
            location = location,
            createdAt = createdAt
        ),
        idempotencyKey = idempotencyKey
    )

    suspend fun uploadEvidence(
        inspectionId: String,
        evidenceId: String,
        type: String,
        localUri: String?,
        note: String?,
        idempotencyKey: String
    ): UploadEvidenceResponse {

        return api.uploadEvidence(
            inspectionId = inspectionId,
            request = mapOf(
                "id" to evidenceId,
                "type" to type,
                "localUri" to localUri,
                "note" to note
            ),
            idempotencyKey = idempotencyKey
        )
    }
}