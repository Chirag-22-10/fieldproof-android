package com.spacedrift.fieldproof.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey
    val id: String,

    val inspectionId: String,

    val title: String,

    val required: Boolean = true,

    val completed: Boolean = false
)