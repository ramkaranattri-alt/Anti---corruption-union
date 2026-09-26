package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CorruptionDao
import com.example.data.local.entity.ActivistMessageEntity
import com.example.data.local.entity.AnonymousReportEntity
import com.example.data.local.entity.CitizenPetitionEntity
import com.example.data.local.entity.CorruptionReportEntity
import com.example.data.local.entity.SatireMemeEntity

@Database(
    entities = [
        CorruptionReportEntity::class,
        AnonymousReportEntity::class,
        ActivistMessageEntity::class,
        SatireMemeEntity::class,
        CitizenPetitionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun corruptionDao(): CorruptionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "indian_anti_corruption_union.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
