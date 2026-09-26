package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "satire_memes")
data class SatireMemeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val speaker: String,
    val role: String,
    val quote: String,
    val context: String,
    val speechAudioPrompt: String,
    val laughsCount: Int = 420,
    val category: String = "POLITICAL JOKE",
    val avatarEmoji: String = "🎭",
    val timestamp: Long = System.currentTimeMillis()
)
