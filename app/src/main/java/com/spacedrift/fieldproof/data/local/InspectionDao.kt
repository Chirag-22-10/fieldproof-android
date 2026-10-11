package com.spacedrift.fieldproof.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InspectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: InspectionEntity)

    @Query("SELECT * FROM inspections ORDER BY updatedAt DESC")
    fun observeInspections(): Flow<List<InspectionEntity>>

    @Query("SELECT * FROM inspections WHERE id = :inspectionId LIMIT 1")
    suspend fun getInspection(
        inspectionId: String
    ): InspectionEntity?

    @Query("DELETE FROM inspections WHERE id = :inspectionId")
    suspend fun deleteInspection(
        inspectionId: String
    )
}