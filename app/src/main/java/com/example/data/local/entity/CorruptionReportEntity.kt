package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "corruption_reports")
data class CorruptionReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val department: String,
    val location: String,
    val description: String,
    val whistleblowerName: String,
    val isAnonymous: Boolean = true,
    val videoDuration: String = "02:15 min",
    val videoUri: String? = null,
    val thumbnailPlaceholder: String = "evidence_video",
    val upvotes: Int = 120,
    val shares: Int = 45,
    val isVerified: Boolean = true,
    val statusTag: String = "VERIFIED EXPOSE",
    val targetOfficial: String = "Public Sector / Ministry",
    val timestamp: Long = System.currentTimeMillis()
)
