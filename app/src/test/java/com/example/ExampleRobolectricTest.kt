package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.entity.CorruptionReportEntity
import com.example.data.local.entity.SatireMemeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("IACU Union", appName)
    }

    @Test
    fun testInsertAndRetrieveCorruptionReport() = runBlocking {
        val dao = db.corruptionDao()
        val report = CorruptionReportEntity(
            title = "Test Paper Leak Scam",
            department = "Education & NEET",
            location = "Patna",
            description = "OMR sheet tampering investigation",
            whistleblowerName = "Anonymous Whistleblower #12",
            isAnonymous = true
        )
        val id = dao.insertReport(report)
        assertTrue(id > 0)

        val allReports = dao.getAllReports().first()
        assertEquals(1, allReports.size)
        assertEquals("Test Paper Leak Scam", allReports[0].title)
        assertTrue(allReports[0].isAnonymous)
    }

    @Test
    fun testSatireMemeQuotes() = runBlocking {
        val dao = db.corruptionDao()
        val dharmendraMeme = SatireMemeEntity(
            speaker = "Dharmendra Pradhan",
            role = "Union Education Minister",
            quote = "idk about education here my children are abroad",
            context = "NEET paper leak protests",
            speechAudioPrompt = "I don't know about education here, my children are abroad!"
        )
        val modiMeme = SatireMemeEntity(
            speaker = "Narendra Modi",
            role = "Prime Minister",
            quote = "hello frends",
            context = "Addressing the nation",
            speechAudioPrompt = "Hello frends! Chai peelo!"
        )
        dao.insertMemes(listOf(dharmendraMeme, modiMeme))

        val memes = dao.getAllMemes().first()
        assertEquals(2, memes.size)
        assertEquals("idk about education here my children are abroad", memes[0].quote)
        assertEquals("hello frends", memes[1].quote)
    }
}
