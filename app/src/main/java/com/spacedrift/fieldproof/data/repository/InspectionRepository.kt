package com.spacedrift.fieldproof.data.repository

import com.spacedrift.fieldproof.data.local.ChecklistItemDao
import com.spacedrift.fieldproof.data.local.ChecklistItemEntity
import com.spacedrift.fieldproof.data.local.EvidenceDao
import com.spacedrift.fieldproof.data.local.EvidenceEntity
import com.spacedrift.fieldproof.data.local.InspectionDao
import com.spacedrift.fieldproof.data.local.InspectionEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class InspectionRepository @Inject constructor(
    private val inspectionDao: InspectionDao,
    private val checklistItemDao: ChecklistItemDao,
    private val evidenceDao: EvidenceDao,
    private val syncRepository: SyncRepository
) {

    fun observeInspections(): Flow<List<InspectionEntity>> {
        return inspectionDao.observeInspections()
    }

    suspend fun getInspection(
        inspectionId: String
    ): InspectionEntity? {
        return inspectionDao.getInspection(inspectionId)
    }

    suspend fun createInspection(
        title: String,
        location: String
    ): InspectionEntity {

        val now = System.currentTimeMillis()

        val inspection = InspectionEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            location = location,
            status = "DRAFT",
            createdAt = now,
            updatedAt = now
        )

        inspectionDao.insertInspection(inspection)

        createDefaultChecklist(inspection.id)

        return inspection
    }

    fun observeChecklist(
        inspectionId: String
    ): Flow<List<ChecklistItemEntity>> {
        return checklistItemDao.observeChecklistItems(inspectionId)
    }

    fun observeEvidence(
        inspectionId: String
    ): Flow<List<EvidenceEntity>> {
        return evidenceDao.observeEvidence(inspectionId)
    }

    suspend fun completeChecklistItem(
        itemId: String,
        completed: Boolean
    ) {
        checklistItemDao.setCompleted(
            itemId = itemId,
            completed = completed
        )
    }

    suspend fun addEvidence(
        inspectionId: String,
        checklistItemId: String?,
        type: String,
        localUri: String?,
        note: String?
    ) {
        val evidenceId = UUID.randomUUID().toString()

        val evidence = EvidenceEntity(
            id = evidenceId,
            inspectionId = inspectionId,
            checklistItemId = checklistItemId,
            type = type,
            localUri = localUri,
            note = note,
            capturedAt = System.currentTimeMillis(),
            syncStatus = "QUEUED"
        )

        // 1. Save the actual evidence locally.
        evidenceDao.insertEvidence(evidence)

        // 2. Create a durable sync operation.
        syncRepository.queueEvidence(evidenceId)
    }

    suspend fun deleteInspection(
        inspectionId: String
    ) {
        checklistItemDao.deleteForInspection(inspectionId)
        evidenceDao.deleteForInspection(inspectionId)
        inspectionDao.deleteInspection(inspectionId)
    }

    private suspend fun createDefaultChecklist(
        inspectionId: String
    ) {
        val items = listOf(
            ChecklistItemEntity(
                id = UUID.randomUUID().toString(),
                inspectionId = inspectionId,
                title = "Equipment condition",
                required = true
            ),
            ChecklistItemEntity(
                id = UUID.randomUUID().toString(),
                inspectionId = inspectionId,
                title = "Safety signage",
                required = true
            ),
            ChecklistItemEntity(
                id = UUID.randomUUID().toString(),
                inspectionId = inspectionId,
                title = "Access and clearance",
                required = true
            ),
            ChecklistItemEntity(
                id = UUID.randomUUID().toString(),
                inspectionId = inspectionId,
                title = "Documentation",
                required = false
            )
        )

        checklistItemDao.insertChecklistItems(items)
    }
}