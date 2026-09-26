package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ActivistMessageEntity
import com.example.data.local.entity.AnonymousReportEntity
import com.example.data.local.entity.CitizenPetitionEntity
import com.example.data.local.entity.CorruptionReportEntity
import com.example.data.local.entity.SatireMemeEntity
import com.example.data.repository.CorruptionRepository
import com.example.util.TtsSoundManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    EXPOSES,
    ANONYMOUS_REPORT,
    ACTIVIST_CONNECT,
    SATIRE_MEMES
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CorruptionRepository
    val ttsSoundManager = TtsSoundManager(application)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = CorruptionRepository(db.corruptionDao())
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Navigation & Modal State
    private val _currentScreen = MutableStateFlow(AppScreen.EXPOSES)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _showAntiGyaneshModal = MutableStateFlow(false)
    val showAntiGyaneshModal: StateFlow<Boolean> = _showAntiGyaneshModal.asStateFlow()

    private val _showModeratorVaultModal = MutableStateFlow(false)
    val showModeratorVaultModal: StateFlow<Boolean> = _showModeratorVaultModal.asStateFlow()

    private val _selectedDepartment = MutableStateFlow("ALL")
    val selectedDepartment: StateFlow<String> = _selectedDepartment.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Currently playing/viewed report
    private val _activePlayingReport = MutableStateFlow<CorruptionReportEntity?>(null)
    val activePlayingReport: StateFlow<CorruptionReportEntity?> = _activePlayingReport.asStateFlow()

    // --- Anonymous Report Form State ---
    private val _anonTitle = MutableStateFlow("")
    val anonTitle: StateFlow<String> = _anonTitle.asStateFlow()

    private val _anonCategory = MutableStateFlow("Education & NEET")
    val anonCategory: StateFlow<String> = _anonCategory.asStateFlow()

    private val _anonLocation = MutableStateFlow("")
    val anonLocation: StateFlow<String> = _anonLocation.asStateFlow()

    private val _anonAccused = MutableStateFlow("")
    val anonAccused: StateFlow<String> = _anonAccused.asStateFlow()

    private val _anonDesc = MutableStateFlow("")
    val anonDesc: StateFlow<String> = _anonDesc.asStateFlow()

    private val _anonHasMedia = MutableStateFlow(false)
    val anonHasMedia: StateFlow<Boolean> = _anonHasMedia.asStateFlow()

    private val _anonMediaType = MutableStateFlow<String?>("VIDEO")
    val anonMediaType: StateFlow<String?> = _anonMediaType.asStateFlow()

    private val _anonMediaName = MutableStateFlow<String?>("corruption_proof.mp4")
    val anonMediaName: StateFlow<String?> = _anonMediaName.asStateFlow()

    private val _submittedTrackingToken = MutableStateFlow<String?>(null)
    val submittedTrackingToken: StateFlow<String?> = _submittedTrackingToken.asStateFlow()

    // Moderator Vault Filter
    private val _moderatorFilter = MutableStateFlow("ALL")
    val moderatorFilter: StateFlow<String> = _moderatorFilter.asStateFlow()

    // Community message input
    private val _activistMessageInput = MutableStateFlow("")
    val activistMessageInput: StateFlow<String> = _activistMessageInput.asStateFlow()

    private val _activistNameInput = MutableStateFlow("Citizen Patriot")
    val activistNameInput: StateFlow<String> = _activistNameInput.asStateFlow()

    private val _activistStateInput = MutableStateFlow("New Delhi")
    val activistStateInput: StateFlow<String> = _activistStateInput.asStateFlow()

    // Repositories Data
    val allReports: StateFlow<List<CorruptionReportEntity>> = combine(
        repository.allReports,
        _selectedDepartment,
        _searchQuery
    ) { reports, dept, query ->
        reports.filter { report ->
            val matchDept = (dept == "ALL" || report.department.equals(dept, ignoreCase = true))
            val matchQuery = query.isEmpty() ||
                    report.title.contains(query, ignoreCase = true) ||
                    report.description.contains(query, ignoreCase = true) ||
                    report.location.contains(query, ignoreCase = true)
            matchDept && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val anonymousReports: StateFlow<List<AnonymousReportEntity>> = combine(
        repository.allAnonymousReports,
        _moderatorFilter
    ) { reports, filter ->
        if (filter == "ALL") reports else reports.filter { it.moderationStatus.equals(filter, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activistMessages: StateFlow<List<ActivistMessageEntity>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val satireMemes: StateFlow<List<SatireMemeEntity>> = repository.allMemes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val citizenPetitions: StateFlow<List<CitizenPetitionEntity>> = repository.petitions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setDepartment(dept: String) {
        _selectedDepartment.value = dept
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openAntiGyaneshModal() {
        _showAntiGyaneshModal.value = true
    }

    fun closeAntiGyaneshModal() {
        _showAntiGyaneshModal.value = false
    }

    fun toggleModeratorVault(show: Boolean) {
        _showModeratorVaultModal.value = show
    }

    fun setModeratorFilter(filter: String) {
        _moderatorFilter.value = filter
    }

    fun setActivePlayingReport(report: CorruptionReportEntity?) {
        _activePlayingReport.value = report
    }

    fun upvoteReport(id: Long) {
        viewModelScope.launch {
            repository.upvoteReport(id)
            if (_activePlayingReport.value?.id == id) {
                _activePlayingReport.value = _activePlayingReport.value?.copy(
                    upvotes = (_activePlayingReport.value?.upvotes ?: 0) + 1
                )
            }
        }
    }

    fun laughAtMeme(id: Long) {
        viewModelScope.launch {
            repository.laughAtMeme(id)
        }
    }

    fun signPetition(id: Long) {
        viewModelScope.launch {
            repository.signPetition(id)
        }
    }

    // Sound / Voice playback for Jokes
    fun playJokeVoice(meme: SatireMemeEntity) {
        when {
            meme.speaker.contains("Dharmendra", ignoreCase = true) -> {
                ttsSoundManager.speakQuote(
                    text = "I don't know about education here, my children are abroad!",
                    pitch = 0.9f,
                    speechRate = 0.95f
                )
            }
            meme.speaker.contains("Modi", ignoreCase = true) -> {
                ttsSoundManager.speakQuote(
                    text = "Hello frends! Welcome to our masterclass!",
                    pitch = 1.25f,
                    speechRate = 0.85f
                )
            }
            meme.speaker.contains("Gyanesh", ignoreCase = true) -> {
                ttsSoundManager.speakQuote(
                    text = "Voter turnout will take two weeks, we are busy with calculations!",
                    pitch = 1.0f,
                    speechRate = 0.95f
                )
            }
            else -> {
                ttsSoundManager.speakQuote(meme.speechAudioPrompt, pitch = 1.05f, speechRate = 1.0f)
            }
        }
    }

    // --- Anonymous Report Submission ---
    fun updateAnonTitle(v: String) { _anonTitle.value = v }
    fun updateAnonCategory(v: String) { _anonCategory.value = v }
    fun updateAnonLocation(v: String) { _anonLocation.value = v }
    fun updateAnonAccused(v: String) { _anonAccused.value = v }
    fun updateAnonDesc(v: String) { _anonDesc.value = v }

    fun setAnonMediaAttachment(hasMedia: Boolean, type: String? = null, name: String? = null) {
        _anonHasMedia.value = hasMedia
        _anonMediaType.value = type
        _anonMediaName.value = name
    }

    fun dismissTrackingTokenDialog() {
        _submittedTrackingToken.value = null
    }

    fun submitAnonymousReport() {
        if (_anonTitle.value.isBlank() || _anonDesc.value.isBlank()) return

        val randId = (1000..9999).random()
        val token = "IACU-ANON-$randId-${UUID.randomUUID().toString().take(4).uppercase()}"

        val report = AnonymousReportEntity(
            trackingToken = token,
            title = _anonTitle.value.trim(),
            category = _anonCategory.value,
            location = if (_anonLocation.value.isBlank()) "India" else _anonLocation.value.trim(),
            description = _anonDesc.value.trim(),
            accusedEntity = if (_anonAccused.value.isBlank()) "Public Entity / Authorities" else _anonAccused.value.trim(),
            hasMediaAttachment = _anonHasMedia.value,
            mediaType = _anonMediaType.value,
            mediaFileName = _anonMediaName.value,
            mediaUri = if (_anonHasMedia.value) "content://iacu/secure_evidence/$token" else null,
            isEncrypted = true,
            moderationStatus = "PENDING_REVIEW",
            moderatorNotes = "Fresh citizen submission. Identity scrubbed.",
            priority = "HIGH"
        )

        viewModelScope.launch {
            repository.insertAnonymousReport(report)
            _submittedTrackingToken.value = token
            _anonTitle.value = ""
            _anonDesc.value = ""
            _anonLocation.value = ""
            _anonAccused.value = ""
            _anonHasMedia.value = false
        }
    }

    // --- Moderator Actions ---
    fun approveAndPublishToPublicFeed(report: AnonymousReportEntity) {
        viewModelScope.launch {
            repository.publishAnonymousReportToPublicFeed(report)
        }
    }

    fun setReportUnderInvestigation(report: AnonymousReportEntity, notes: String = "Under verification by legal & RTI wing.") {
        viewModelScope.launch {
            repository.updateModerationStatus(report.id, "INVESTIGATION", notes)
        }
    }

    fun deleteAnonymousReport(id: Long) {
        viewModelScope.launch {
            repository.deleteAnonymousReport(id)
        }
    }

    // Activist chat
    fun updateActivistMessage(v: String) { _activistMessageInput.value = v }
    fun updateActivistName(v: String) { _activistNameInput.value = v }
    fun updateActivistState(v: String) { _activistStateInput.value = v }

    fun postActivistMessage() {
        if (_activistMessageInput.value.isBlank()) return

        val msg = ActivistMessageEntity(
            senderName = if (_activistNameInput.value.isBlank()) "Citizen Activist" else _activistNameInput.value.trim(),
            roleTag = "Citizen Whistleblower",
            state = if (_activistStateInput.value.isBlank()) "India" else _activistStateInput.value.trim(),
            message = _activistMessageInput.value.trim(),
            isVerifiedActivist = true
        )

        viewModelScope.launch {
            repository.postActivistMessage(msg)
            _activistMessageInput.value = ""
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsSoundManager.release()
    }
}
