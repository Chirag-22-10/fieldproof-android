package com.spacedrift.fieldproof.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inspections")
data class InspectionEntity(
    @PrimaryKey
    val id: String,

    val title: String,

    val location: String,

    val status: String = "DRAFT",

    val createdAt: Long,

    val updatedAt: Long
)