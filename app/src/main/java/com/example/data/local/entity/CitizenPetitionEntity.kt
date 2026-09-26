package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citizen_petitions")
data class CitizenPetitionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val petitionTitle: String,
    val demandSummary: String,
    val targetAuthority: String,
    val signaturesCount: Int = 18450,
    val hasUserSigned: Boolean = false
)
