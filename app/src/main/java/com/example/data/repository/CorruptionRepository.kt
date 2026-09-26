package com.example.data.repository

import com.example.data.local.dao.CorruptionDao
import com.example.data.local.entity.ActivistMessageEntity
import com.example.data.local.entity.AnonymousReportEntity
import com.example.data.local.entity.CitizenPetitionEntity
import com.example.data.local.entity.CorruptionReportEntity
import com.example.data.local.entity.SatireMemeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CorruptionRepository(private val dao: CorruptionDao) {

    val allReports: Flow<List<CorruptionReportEntity>> = dao.getAllReports()
    val allAnonymousReports: Flow<List<AnonymousReportEntity>> = dao.getAllAnonymousReports()
    val allMessages: Flow<List<ActivistMessageEntity>> = dao.getAllMessages()
    val allMemes: Flow<List<SatireMemeEntity>> = dao.getAllMemes()
    val petitions: Flow<List<CitizenPetitionEntity>> = dao.getPetitions()

    fun getReportsByDept(dept: String): Flow<List<CorruptionReportEntity>> {
        return if (dept == "ALL" || dept.isEmpty()) dao.getAllReports() else dao.getReportsByDepartment(dept)
    }

    suspend fun insertReport(report: CorruptionReportEntity): Long = dao.insertReport(report)

    suspend fun upvoteReport(id: Long) = dao.upvoteReport(id)

    suspend fun deleteReport(id: Long) = dao.deleteReport(id)

    // Secure Anonymous Reporting & Moderation
    suspend fun insertAnonymousReport(report: AnonymousReportEntity): Long = dao.insertAnonymousReport(report)

    suspend fun updateModerationStatus(id: Long, status: String, notes: String) {
        dao.updateModerationStatus(id, status, notes)
    }

    suspend fun deleteAnonymousReport(id: Long) = dao.deleteAnonymousReport(id)

    suspend fun publishAnonymousReportToPublicFeed(anonReport: AnonymousReportEntity) {
        val publicReport = CorruptionReportEntity(
            title = anonReport.title,
            department = anonReport.category,
            location = anonReport.location,
            description = anonReport.description,
            whistleblowerName = "Anonymous Whistleblower (${anonReport.trackingToken})",
            isAnonymous = true,
            videoDuration = if (anonReport.hasMediaAttachment) "02:30 min" else "Document Dossier",
            videoUri = anonReport.mediaUri,
            upvotes = 10,
            shares = 2,
            isVerified = true,
            statusTag = "MODERATOR VERIFIED",
            targetOfficial = anonReport.accusedEntity
        )
        dao.insertReport(publicReport)
        dao.updateModerationStatus(
            anonReport.id,
            "VERIFIED_PUBLIC",
            "Verified by Union Moderator and published to public feed."
        )
    }

    suspend fun postActivistMessage(message: ActivistMessageEntity): Long = dao.insertMessage(message)

    suspend fun laughAtMeme(id: Long) = dao.incrementLaughs(id)

    suspend fun signPetition(id: Long) = dao.signPetition(id)

    suspend fun seedInitialDataIfEmpty() {
        val existingReports = dao.getAllReports().firstOrNull()
        if (existingReports.isNullOrEmpty()) {
            val initialReports = listOf(
                CorruptionReportEntity(
                    id = 1,
                    title = "NEET-UG & NET Paper Leak Coverup: Solved Papers Sold in Bihar & Gujarat",
                    department = "Education & NEET",
                    location = "Patna & Godhra",
                    description = "Comprehensive whistleblower dossier revealing organized mafia leaking question papers before exams. Arrested accused confessed to paying ₹30-40 lakhs per candidate while education authorities dismissed irregularities.",
                    whistleblowerName = "Student Rights Collective",
                    isAnonymous = false,
                    videoDuration = "03:42 min",
                    upvotes = 3420,
                    shares = 812,
                    isVerified = true,
                    statusTag = "BREAKING EXPOSE",
                    targetOfficial = "Ministry of Education & NTA"
                ),
                CorruptionReportEntity(
                    id = 2,
                    title = "Electoral Bonds Racket: ₹16,500 Cr Extortion Scheme Linked to ED Raids",
                    department = "Electoral Bonds & Finance",
                    location = "New Delhi",
                    description = "SBI disclosure data cross-matched with central investigative agency raids shows corporate entities bought hundreds of crores in electoral bonds immediately after being raided by ED/IT agencies, receiving mega highway tenders within weeks.",
                    whistleblowerName = "Association for Democratic Reforms Watch",
                    isAnonymous = false,
                    videoDuration = "05:18 min",
                    upvotes = 5120,
                    shares = 1430,
                    isVerified = true,
                    statusTag = "DOCUMENTED SCANDAL",
                    targetOfficial = "Central Ruling Leadership & SBI"
                ),
                CorruptionReportEntity(
                    id = 3,
                    title = "Gyanesh Kumar ECI Controversy: CJI Dropped from Panel & Form 17C Hidden",
                    department = "Elections & ECI",
                    location = "New Delhi / Nirvachan Sadan",
                    description = "Analysis of the appointment of CEC Gyanesh Kumar under the new 2023 law which eliminated the Chief Justice of India from the selection committee. Details the subsequent refusal to release prompt booth-wise voter turnout numbers (Form 17C).",
                    whistleblowerName = "Electoral Integrity Initiative",
                    isAnonymous = true,
                    videoDuration = "04:05 min",
                    upvotes = 4690,
                    shares = 1205,
                    isVerified = true,
                    statusTag = "WATCHDOG ALERT",
                    targetOfficial = "Chief Election Commissioner Gyanesh Kumar"
                ),
                CorruptionReportEntity(
                    id = 4,
                    title = "15 Bridges Collapse in 3 Weeks: Substandard Cement & 40% Kickback Syndicate",
                    department = "Infrastructure & Tenders",
                    location = "Bihar & Gujarat",
                    description = "Newly inaugurated bridges collapse like a house of cards before public use. Whistleblower engineers provide laboratory test results showing 60% sand dilution and fake fitness certifications signed without site inspections.",
                    whistleblowerName = "Civil Engineers Against Graft",
                    isAnonymous = true,
                    videoDuration = "02:30 min",
                    upvotes = 2890,
                    shares = 760,
                    isVerified = true,
                    statusTag = "GROUND VIDEO EVIDENCE",
                    targetOfficial = "State PWD & Blacklisted Contractors"
                ),
                CorruptionReportEntity(
                    id = 5,
                    title = "Smart City Fund Diversion: ₹800 Cr Spent on Paper, Flooding in Reality",
                    department = "Urban Development",
                    location = "Uttar Pradesh & MP",
                    description = "Drainage modernization and smart surveillance contracts awarded to newly formed political proxy firms. Drone footage exposes zero desilting work carried out despite 100% budget drawdown.",
                    whistleblowerName = "Urban Transparency Front",
                    isAnonymous = true,
                    videoDuration = "03:15 min",
                    upvotes = 1940,
                    shares = 410,
                    isVerified = true,
                    statusTag = "VERIFIED AUDIT",
                    targetOfficial = "Smart City SPV & Municipal Corporations"
                )
            )
            dao.insertReports(initialReports)
        }

        val existingAnonReports = dao.getAllAnonymousReports().firstOrNull()
        if (existingAnonReports.isNullOrEmpty()) {
            val initialAnon = listOf(
                AnonymousReportEntity(
                    id = 1,
                    trackingToken = "IACU-ANON-9412-B7",
                    title = "Substandard Concrete Dilution in Expressway Overbridge",
                    category = "Infrastructure & Tenders",
                    location = "Moradabad, Uttar Pradesh",
                    description = "Inspection report shows contractors substituted 43-grade cement with fly-ash slurry without engineer certification. Submitting video recording of cracks developing within 14 days of casting.",
                    accusedEntity = "Chief Project Engineer & Private Concessionaire",
                    hasMediaAttachment = true,
                    mediaType = "VIDEO",
                    mediaFileName = "overbridge_structural_cracks.mp4",
                    mediaUri = "content://media/external/video/crack_proof",
                    isEncrypted = true,
                    moderationStatus = "PENDING_REVIEW",
                    moderatorNotes = "Awaiting verification by civil engineering cell.",
                    priority = "HIGH"
                ),
                AnonymousReportEntity(
                    id = 2,
                    trackingToken = "IACU-ANON-3281-K9",
                    title = "Government Medical College Equipment Procurement 300% Over-Invoicing",
                    category = "Healthcare & Hospitals",
                    location = "Indore, Madhya Pradesh",
                    description = "Purchase orders for dialysis units inflated from market rate of ₹6.5 Lakhs to ₹21 Lakhs each. Photo of approved supply tender bills attached anonymously.",
                    accusedEntity = "Hospital Superintendent & Tender Committee",
                    hasMediaAttachment = true,
                    mediaType = "PHOTO",
                    mediaFileName = "invoice_discrepancy_scan.jpg",
                    mediaUri = "content://media/external/images/tender_doc",
                    isEncrypted = true,
                    moderationStatus = "INVESTIGATION",
                    moderatorNotes = "Whistleblower submitted vendor quotations. Forwarding to RTI legal cell.",
                    priority = "CRITICAL"
                )
            )
            dao.insertAnonymousReports(initialAnon)
        }

        val existingMessages = dao.getAllMessages().firstOrNull()
        if (existingMessages.isNullOrEmpty()) {
            val initialMessages = listOf(
                ActivistMessageEntity(
                    id = 1,
                    senderName = "Advocate Prashant K.",
                    roleTag = "Supreme Court Litigator",
                    state = "Delhi",
                    message = "We have prepared the Public Interest Litigation challenging the exclusion of the CJI from the ECI selection committee. Gyanesh Kumar must be held accountable for voter turnout data transparency!"
                ),
                ActivistMessageEntity(
                    id = 2,
                    senderName = "Prof. Sunita Deshmukh",
                    roleTag = "RTI Federation",
                    state = "Maharashtra",
                    message = "RTI query filed regarding ₹2,400 Cr BMC road concrete contracts. If anyone has site video evidence of potholes on newly inaugurated roads, submit here directly!"
                ),
                ActivistMessageEntity(
                    id = 3,
                    senderName = "Ayush Kumar (NEET Aspirant)",
                    roleTag = "Student Whistleblower",
                    state = "Bihar",
                    message = "Dharmendra Pradhan says education is fine while our exam papers leak every single year! Proud to see our union collecting video proof of rigging."
                ),
                ActivistMessageEntity(
                    id = 4,
                    senderName = "Kavitha R.",
                    roleTag = "Citizen Watchdog",
                    state = "Karnataka",
                    message = "Remember to keep Anonymous Mode ON when recording government offices demanding speed money or bribes. The union scrubs metadata automatically."
                ),
                ActivistMessageEntity(
                    id = 5,
                    senderName = "Harpreet Singh",
                    roleTag = "Farmers & Civic Union",
                    state = "Punjab",
                    message = "Standing united against crony monopolies. The 'Anti Gyanesh Kumar' transparency petition has already crossed 18,000 signatures!"
                )
            )
            dao.insertMessages(initialMessages)
        }

        val existingMemes = dao.getAllMemes().firstOrNull()
        if (existingMemes.isNullOrEmpty()) {
            val initialMemes = listOf(
                SatireMemeEntity(
                    id = 1,
                    speaker = "Dharmendra Pradhan",
                    role = "Union Education Minister",
                    quote = "idk about education here my children are abroad",
                    context = "When students protested NEET & UGC-NET exam leaks and demanded accountability for millions of ruined careers.",
                    speechAudioPrompt = "I don't know about education here, my children are abroad!",
                    laughsCount = 3840,
                    category = "EDUCATION SATIRE",
                    avatarEmoji = "🎓"
                ),
                SatireMemeEntity(
                    id = 2,
                    speaker = "Narendra Modi",
                    role = "Prime Minister",
                    quote = "hello frends",
                    context = "Viral comedic parody style addressing the nation, promising 100% corruption-free cloud radar governance while dodging unscripted press conferences.",
                    speechAudioPrompt = "Hello frends! Chai peelo, and welcome to our zero-press-conference masterclass!",
                    laughsCount = 5920,
                    category = "POLITICAL JOKE",
                    avatarEmoji = "🎙️"
                ),
                SatireMemeEntity(
                    id = 3,
                    speaker = "Chief Election Commissioner Gyanesh Kumar",
                    role = "Head of Nirvachan Sadan",
                    quote = "Voter turnout jumped 6% at midnight? That's just spiritual democracy!",
                    context = "Explaining mysterious 1.07 crore voter surge between 7 PM polling close and delayed final turnout release.",
                    speechAudioPrompt = "Voter turnout jumped at midnight? That is just the magic of election management!",
                    laughsCount = 2150,
                    category = "ECI WATCHDOG",
                    avatarEmoji = "🗳️"
                ),
                SatireMemeEntity(
                    id = 4,
                    speaker = "Chief Bridge Engineer",
                    role = "PWD & State Contractors",
                    quote = "The bridge did not collapse due to corruption, it was testing submarine capabilities!",
                    context = "15 bridges crumbling into rivers across Bihar and Gujarat within 2 weeks of construction.",
                    speechAudioPrompt = "The bridge did not collapse, it was simply conducting underwater research!",
                    laughsCount = 1890,
                    category = "INFRASTRUCTURE MEME",
                    avatarEmoji = "🌉"
                ),
                SatireMemeEntity(
                    id = 5,
                    speaker = "Finance Ministry Spokesperson",
                    role = "Electoral Bonds Master",
                    quote = "Electoral Bonds were created for 100% transparency. That's why we fought tooth and nail to hide who bought them!",
                    context = "Supreme Court struck down Electoral Bonds scheme as unconstitutional and legalized bribery.",
                    speechAudioPrompt = "Electoral bonds were totally transparent, which is why donor names were strictly confidential!",
                    laughsCount = 2780,
                    category = "BONDS SCANDAL",
                    avatarEmoji = "💰"
                )
            )
            dao.insertMemes(initialMemes)
        }

        val existingPetitions = dao.getPetitions().firstOrNull()
        if (existingPetitions.isNullOrEmpty()) {
            val initialPetitions = listOf(
                CitizenPetitionEntity(
                    id = 1,
                    petitionTitle = "ANTI GYANESH KUMAR: Re-instate Supreme Court CJI on ECI Selection Panel",
                    demandSummary = "The Election Commission of India must remain fiercely independent. We demand the immediate revocation of the law removing the Chief Justice of India from appointing Election Commissioners, and mandate 24-hour publication of Form 17C booth-level voter records.",
                    targetAuthority = "Election Commission of India & Supreme Court Constitution Bench",
                    signaturesCount = 18450,
                    hasUserSigned = false
                ),
                CitizenPetitionEntity(
                    id = 2,
                    petitionTitle = "Independent Special Investigation Team for NEET & NET Paper Rigging",
                    demandSummary = "Replace NTA leadership, blacklist tainted private testing centers, compensate affected students, and prosecute criminal syndicates under strict anti-corruption laws.",
                    targetAuthority = "Ministry of Education & Central Bureau of Investigation",
                    signaturesCount = 24120,
                    hasUserSigned = false
                )
            )
            dao.insertPetitions(initialPetitions)
        }
    }
}
