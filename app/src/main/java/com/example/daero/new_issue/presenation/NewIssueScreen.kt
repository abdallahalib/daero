package com.example.daero.new_issue.presenation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daero.R
import com.example.daero.issue_list.domain.model.IssuePriority
import com.example.daero.issue_list.domain.model.IssueStatus
import com.example.daero.new_issue.NewIssueEffect
import com.example.daero.new_issue.NewIssueIntent
import com.example.daero.new_issue.NewIssueViewModel
import com.example.daero.shared.presenation.AddNotesContent
import com.example.daero.shared.presenation.TakePhotoContent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIssueScreen(
    newIssueViewModel: NewIssueViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
) {
    val newIssueUiState = newIssueViewModel.uiState.collectAsStateWithLifecycle().value
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val titleState = rememberTextFieldState()
    val notesState = rememberTextFieldState()
    val locationState = rememberTextFieldState()
    var selectedPriority by remember { mutableStateOf<IssuePriority>(IssuePriority.MEDIUM) }
    var selectedStatus by remember { mutableStateOf<IssueStatus>(IssueStatus.OPEN) }
    LaunchedEffect(newIssueUiState.currentPage) {
        if (newIssueUiState.currentPage != pagerState.currentPage) {
            pagerState.animateScrollToPage(newIssueUiState.currentPage)
        }
    }
    LaunchedEffect(Unit) {
        newIssueViewModel.effect.collect {
            when (it) {
                is NewIssueEffect.NavigateBack -> {
                    onBackClicked()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create Field Note")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            newIssueViewModel.handleIntent(NewIssueIntent.OnBackClicked)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                            contentDescription = "Sync"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues),
            userScrollEnabled = false
        ) {
            when (it) {
                0 -> TakePhotoContent(
                    preview = newIssueViewModel.preview,
                    surfaceRequest = newIssueUiState.surfaceRequest,
                    imageCapture = newIssueViewModel.imageCapture,
                    onCaptureClicked = {
                        newIssueViewModel.handleIntent(NewIssueIntent.OnCapturePhotoClicked)
                    },
                    capturedImage = newIssueUiState.capturedImage,
                    isCapturing = newIssueUiState.isCapturing,
                    onRetakeClicked = {
                        newIssueViewModel.handleIntent(NewIssueIntent.OnRetakeClicked)
                    },
                    onConfirmClicked = {
                        newIssueViewModel.handleIntent(NewIssueIntent.OnSubmitClicked)
                    },
                )

                1 -> AddNotesContent(
                    title = titleState,
                    notes = notesState,
                    location = locationState,
                    priority = selectedPriority,
                    onPrioritySelected = {
                        selectedPriority = it
                    },
                    status = selectedStatus,
                    onStatusSelected = {
                        selectedStatus = it
                    },
                    onSaveClicked = {
                        newIssueViewModel.handleIntent(
                            NewIssueIntent.OnSaveClicked(
                                title = titleState.text.toString(),
                                notes = notesState.text.toString(),
                                location = locationState.text.toString(),
                                priority = selectedPriority,
                                status = selectedStatus,
                            )
                        )
                    },
                    titleError = newIssueUiState.titleError,
                    notesError = newIssueUiState.notesError,
                    locationError = newIssueUiState.locationError,
                )
            }
        }
        if (newIssueUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}