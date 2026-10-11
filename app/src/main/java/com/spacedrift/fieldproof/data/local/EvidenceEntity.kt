package com.spacedrift.fieldproof.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evidence")
data class EvidenceEntity(
    @PrimaryKey
    val id: String,

    val inspectionId: String,

    val checklistItemId: String?,

    val type: String,

    val localUri: String?,

    val note: String?,

    val capturedAt: Long,

    val syncStatus: String = "QUEUED"
)