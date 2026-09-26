package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AntiGyaneshKumarDialog
import com.example.ui.components.ModeratorVaultModal
import com.example.ui.components.TopNavBar
import com.example.ui.components.VideoPlayerModal
import com.example.ui.screens.ActivistConnectScreen
import com.example.ui.screens.AnonymousReportScreen
import com.example.ui.screens.ExposesScreen
import com.example.ui.screens.SatireMemesScreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.UnionDarkNavy

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val showAntiGyaneshModal by viewModel.showAntiGyaneshModal.collectAsStateWithLifecycle()
    val showModeratorVaultModal by viewModel.showModeratorVaultModal.collectAsStateWithLifecycle()
    val selectedDept by viewModel.selectedDepartment.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val reports by viewModel.allReports.collectAsStateWithLifecycle()
    val anonymousReports by viewModel.anonymousReports.collectAsStateWithLifecycle()
    val moderatorFilter by viewModel.moderatorFilter.collectAsStateWithLifecycle()
    val activistMessages by viewModel.activistMessages.collectAsStateWithLifecycle()
    val satireMemes by viewModel.satireMemes.collectAsStateWithLifecycle()
    val petitions by viewModel.citizenPetitions.collectAsStateWithLifecycle()
    val activePlayingReport by viewModel.activePlayingReport.collectAsStateWithLifecycle()

    // Anonymous Report form states
    val anonTitle by viewModel.anonTitle.collectAsStateWithLifecycle()
    val anonCategory by viewModel.anonCategory.collectAsStateWithLifecycle()
    val anonLocation by viewModel.anonLocation.collectAsStateWithLifecycle()
    val anonAccused by viewModel.anonAccused.collectAsStateWithLifecycle()
    val anonDesc by viewModel.anonDesc.collectAsStateWithLifecycle()
    val anonHasMedia by viewModel.anonHasMedia.collectAsStateWithLifecycle()
    val anonMediaType by viewModel.anonMediaType.collectAsStateWithLifecycle()
    val anonMediaName by viewModel.anonMediaName.collectAsStateWithLifecycle()
    val submittedToken by viewModel.submittedTrackingToken.collectAsStateWithLifecycle()

    val activistMsgInput by viewModel.activistMessageInput.collectAsStateWithLifecycle()
    val activistNameInput by viewModel.activistNameInput.collectAsStateWithLifecycle()
    val activistStateInput by viewModel.activistStateInput.collectAsStateWithLifecycle()

    val antiGyaneshPetition = petitions.firstOrNull { it.petitionTitle.contains("GYANESH", ignoreCase = true) }
        ?: petitions.firstOrNull()

    val pendingCount = anonymousReports.count { it.moderationStatus.equals("PENDING_REVIEW", ignoreCase = true) }

    // Back handling
    BackHandler(enabled = showAntiGyaneshModal || showModeratorVaultModal || activePlayingReport != null || currentScreen != AppScreen.EXPOSES) {
        when {
            showModeratorVaultModal -> viewModel.toggleModeratorVault(false)
            showAntiGyaneshModal -> viewModel.closeAntiGyaneshModal()
            activePlayingReport != null -> viewModel.setActivePlayingReport(null)
            currentScreen != AppScreen.EXPOSES -> viewModel.navigateTo(AppScreen.EXPOSES)
        }
    }

    Scaffold(
        topBar = {
            TopNavBar(
                onAntiGyaneshClick = { viewModel.openAntiGyaneshModal() },
                onModeratorVaultClick = { viewModel.toggleModeratorVault(true) },
                pendingModerationCount = pendingCount
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = UnionDarkNavy,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.EXPOSES,
                    onClick = { viewModel.navigateTo(AppScreen.EXPOSES) },
                    icon = { Icon(imageVector = Icons.Default.Campaign, contentDescription = "Exposes") },
                    label = { Text("Exposes", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = SaffronPrimary,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = SaffronPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_exposes")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.ANONYMOUS_REPORT,
                    onClick = { viewModel.navigateTo(AppScreen.ANONYMOUS_REPORT) },
                    icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Anon Report") },
                    label = { Text("Anon Report", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = EmeraldGreen,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = EmeraldGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_anonymous_report")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.ACTIVIST_CONNECT,
                    onClick = { viewModel.navigateTo(AppScreen.ACTIVIST_CONNECT) },
                    icon = { Icon(imageVector = Icons.Default.Group, contentDescription = "Connect") },
                    label = { Text("Network", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = SaffronPrimary,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = SaffronPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_activists")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.SATIRE_MEMES,
                    onClick = { viewModel.navigateTo(AppScreen.SATIRE_MEMES) },
                    icon = { Icon(imageVector = Icons.Default.EmojiEmotions, contentDescription = "Satire") },
                    label = { Text("Satire", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = SaffronPrimary,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = SaffronPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_satire")
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.EXPOSES -> {
                    ExposesScreen(
                        reports = reports,
                        selectedDept = selectedDept,
                        searchQuery = searchQuery,
                        onDeptSelected = { viewModel.setDepartment(it) },
                        onSearchChanged = { viewModel.setSearchQuery(it) },
                        onWatchVideo = { viewModel.setActivePlayingReport(it) },
                        onUpvote = { viewModel.upvoteReport(it) },
                        onAntiGyaneshClick = { viewModel.openAntiGyaneshModal() },
                        onNavigateToAnonymousReport = { viewModel.navigateTo(AppScreen.ANONYMOUS_REPORT) }
                    )
                }

                AppScreen.ANONYMOUS_REPORT -> {
                    AnonymousReportScreen(
                        title = anonTitle,
                        category = anonCategory,
                        location = anonLocation,
                        accused = anonAccused,
                        description = anonDesc,
                        hasMedia = anonHasMedia,
                        mediaType = anonMediaType,
                        mediaName = anonMediaName,
                        submittedToken = submittedToken,
                        onTitleChange = { viewModel.updateAnonTitle(it) },
                        onCategoryChange = { viewModel.updateAnonCategory(it) },
                        onLocationChange = { viewModel.updateAnonLocation(it) },
                        onAccusedChange = { viewModel.updateAnonAccused(it) },
                        onDescChange = { viewModel.updateAnonDesc(it) },
                        onSetMedia = { has, type, name -> viewModel.setAnonMediaAttachment(has, type, name) },
                        onSubmit = { viewModel.submitAnonymousReport() },
                        onDismissSuccessDialog = { viewModel.dismissTrackingTokenDialog() }
                    )
                }

                AppScreen.ACTIVIST_CONNECT -> {
                    ActivistConnectScreen(
                        messages = activistMessages,
                        messageInput = activistMsgInput,
                        nameInput = activistNameInput,
                        stateInput = activistStateInput,
                        onMessageInputChange = { viewModel.updateActivistMessage(it) },
                        onNameInputChange = { viewModel.updateActivistName(it) },
                        onStateInputChange = { viewModel.updateActivistState(it) },
                        onSendMessage = { viewModel.postActivistMessage() }
                    )
                }

                AppScreen.SATIRE_MEMES -> {
                    SatireMemesScreen(
                        memes = satireMemes,
                        onPlayAudio = { viewModel.playJokeVoice(it) },
                        onLaugh = { viewModel.laughAtMeme(it) },
                        onAntiGyaneshClick = { viewModel.openAntiGyaneshModal() }
                    )
                }
            }
        }

        // Dedicated Anti Gyanesh Kumar Modal Dialog
        if (showAntiGyaneshModal) {
            AntiGyaneshKumarDialog(
                signaturesCount = antiGyaneshPetition?.signaturesCount ?: 18450,
                hasSigned = antiGyaneshPetition?.hasUserSigned ?: false,
                onSignPetition = {
                    antiGyaneshPetition?.let { viewModel.signPetition(it.id) }
                },
                onDismiss = { viewModel.closeAntiGyaneshModal() },
                onReportEciIssue = {
                    viewModel.updateAnonCategory("Elections & ECI")
                    viewModel.navigateTo(AppScreen.ANONYMOUS_REPORT)
                }
            )
        }

        // Dedicated Moderator Vault Modal
        if (showModeratorVaultModal) {
            ModeratorVaultModal(
                reports = anonymousReports,
                selectedFilter = moderatorFilter,
                onFilterChange = { viewModel.setModeratorFilter(it) },
                onApproveAndPublish = { viewModel.approveAndPublishToPublicFeed(it) },
                onInvestigate = { viewModel.setReportUnderInvestigation(it) },
                onDelete = { viewModel.deleteAnonymousReport(it) },
                onDismiss = { viewModel.toggleModeratorVault(false) }
            )
        }

        // Active Evidence Video Player
        activePlayingReport?.let { report ->
            VideoPlayerModal(
                report = report,
                onUpvote = { viewModel.upvoteReport(it) },
                onDismiss = { viewModel.setActivePlayingReport(null) }
            )
        }
    }
}
