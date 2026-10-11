package com.spacedrift.fieldproof.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_operations")
data class SyncOperationEntity(
    @PrimaryKey
    val id: String,
    val entityId: String,
    val entityType: String,
    val operationType: String,
    val status: String = "QUEUED",
    val attempts: Int = 0,
    val lastError: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)