package com.spacedrift.fieldproof.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        InspectionEntity::class,
        ChecklistItemEntity::class,
        EvidenceEntity::class,
        SyncOperationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FieldProofDatabase : RoomDatabase() {

    abstract fun inspectionDao(): InspectionDao

    abstract fun checklistItemDao(): ChecklistItemDao

    abstract fun evidenceDao(): EvidenceDao

    abstract fun syncOperationDao(): SyncOperationDao
}