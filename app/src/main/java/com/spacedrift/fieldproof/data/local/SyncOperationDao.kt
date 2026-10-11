package com.spacedrift.fieldproof.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncOperationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        operation: SyncOperationEntity
    )

    @Query(
        """
        SELECT * FROM sync_operations
        WHERE status = 'QUEUED'
        ORDER BY createdAt ASC
        """
    )
    suspend fun getQueuedOperations(): List<SyncOperationEntity>

    @Query(
        """
        SELECT * FROM sync_operations
        ORDER BY createdAt DESC
        """
    )
    fun observeOperations(): Flow<List<SyncOperationEntity>>

    @Query(
        """
        UPDATE sync_operations
        SET status = :status,
            attempts = :attempts,
            lastError = :lastError,
            updatedAt = :updatedAt
        WHERE id = :operationId
        """
    )
    suspend fun updateStatus(
        operationId: String,
        status: String,
        attempts: Int,
        lastError: String?,
        updatedAt: Long
    )

    @Query(
        """
        DELETE FROM sync_operations
        WHERE id = :operationId
        """
    )
    suspend fun delete(
        operationId: String
    )
}