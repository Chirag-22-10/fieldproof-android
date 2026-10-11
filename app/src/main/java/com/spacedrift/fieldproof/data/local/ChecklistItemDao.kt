package com.spacedrift.fieldproof.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItem(
        item: ChecklistItemEntity
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItems(
        items: List<ChecklistItemEntity>
    )

    @Query(
        "SELECT * FROM checklist_items " +
                "WHERE inspectionId = :inspectionId " +
                "ORDER BY id"
    )
    fun observeChecklistItems(
        inspectionId: String
    ): Flow<List<ChecklistItemEntity>>

    @Query(
        "UPDATE checklist_items " +
                "SET completed = :completed " +
                "WHERE id = :itemId"
    )
    suspend fun setCompleted(
        itemId: String,
        completed: Boolean
    )

    @Query(
        "DELETE FROM checklist_items " +
                "WHERE inspectionId = :inspectionId"
    )
    suspend fun deleteForInspection(
        inspectionId: String
    )
}