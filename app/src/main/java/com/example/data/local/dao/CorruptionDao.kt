package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ActivistMessageEntity
import com.example.data.local.entity.AnonymousReportEntity
import com.example.data.local.entity.CitizenPetitionEntity
import com.example.data.local.entity.CorruptionReportEntity
import com.example.data.local.entity.SatireMemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CorruptionDao {

    // --- Corruption Reports & Videos (Public Feed) ---
    @Query("SELECT * FROM corruption_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<CorruptionReportEntity>>

    @Query("SELECT * FROM corruption_reports WHERE department = :dept ORDER BY timestamp DESC")
    fun getReportsByDepartment(dept: String): Flow<List<CorruptionReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: CorruptionReportEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<CorruptionReportEntity>)

    @Query("UPDATE corruption_reports SET upvotes = upvotes + 1 WHERE id = :id")
    suspend fun upvoteReport(id: Long)

    @Query("DELETE FROM corruption_reports WHERE id = :id")
    suspend fun deleteReport(id: Long)

    // --- Secure Anonymous Reports (Moderation Queue) ---
    @Query("SELECT * FROM anonymous_reports ORDER BY timestamp DESC")
    fun getAllAnonymousReports(): Flow<List<AnonymousReportEntity>>

    @Query("SELECT * FROM anonymous_reports WHERE moderationStatus = :status ORDER BY timestamp DESC")
    fun getAnonymousReportsByStatus(status: String): Flow<List<AnonymousReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnonymousReport(report: AnonymousReportEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnonymousReports(reports: List<AnonymousReportEntity>)

    @Query("UPDATE anonymous_reports SET moderationStatus = :status, moderatorNotes = :notes WHERE id = :id")
    suspend fun updateModerationStatus(id: Long, status: String, notes: String)

    @Query("DELETE FROM anonymous_reports WHERE id = :id")
    suspend fun deleteAnonymousReport(id: Long)

    // --- Activist Community Messages ---
    @Query("SELECT * FROM activist_messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<ActivistMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ActivistMessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ActivistMessageEntity>)

    // --- Satire & Memes ---
    @Query("SELECT * FROM satire_memes ORDER BY id ASC")
    fun getAllMemes(): Flow<List<SatireMemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemes(memes: List<SatireMemeEntity>)

    @Query("UPDATE satire_memes SET laughsCount = laughsCount + 1 WHERE id = :id")
    suspend fun incrementLaughs(id: Long)

    // --- Citizen Petitions (Anti Gyanesh Kumar & ECI Independence) ---
    @Query("SELECT * FROM citizen_petitions ORDER BY id ASC")
    fun getPetitions(): Flow<List<CitizenPetitionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPetitions(petitions: List<CitizenPetitionEntity>)

    @Query("UPDATE citizen_petitions SET signaturesCount = signaturesCount + 1, hasUserSigned = 1 WHERE id = :id")
    suspend fun signPetition(id: Long)
}
