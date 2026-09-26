package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activist_messages")
data class ActivistMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val senderName: String,
    val roleTag: String,
    val state: String,
    val message: String,
    val isVerifiedActivist: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
