package com.example.daero.edit_issue.presentation

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
import com.example.daero.edit_issue.EditIssueEffect
import com.example.daero.edit_issue.EditIssueIntent
import com.example.daero.edit_issue.EditIssueViewModel
import com.example.daero.issue_detail.IssueDetailViewModel
import com.example.daero.shared.presenation.AddNotesContent
import com.example.daero.shared.presenation.TakePhotoContent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditIssueScreen(
    issueId: String,
    onBackClicked: () -> Unit,
    editIssueViewModel: EditIssueViewModel = koinViewModel(parameters = { parametersOf(issueId) }),
) {
    val editIssueUiState = editIssueViewModel.uiState.collectAsStateWithLifecycle().value
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val titleState = rememberTextFieldState(editIssueUiState.title)
    val notesState = rememberTextFieldState(editIssueUiState.notes)
    val locationState = rememberTextFieldState(editIssueUiState.location)
    var selectedPriority by remember { mutableStateOf(editIssueUiState.priority) }
    var selectedStatus by remember { mutableStateOf(editIssueUiState.status) }
    LaunchedEffect(editIssueUiState.currentPage) {
        if (editIssueUiState.currentPage != pagerState.currentPage) {
            pagerState.animateScrollToPage(editIssueUiState.currentPage)
        }
    }
    LaunchedEffect(Unit) {
        editIssueViewModel.effect.collect {
            if (it is EditIssueEffect.NavigateBack) {
                onBackClicked()
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Edit Field Note")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            editIssueViewModel.handleIntent(EditIssueIntent.OnBackClicked)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                            contentDescription = "Back"
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
                    preview = editIssueViewModel.preview,
                    surfaceRequest = editIssueUiState.surfaceRequest,
                    imageCapture = editIssueViewModel.imageCapture,
                    onCaptureClicked = {
                        editIssueViewModel.handleIntent(EditIssueIntent.OnCapturePhotoClicked)
                    },
                    capturedImage = editIssueUiState.capturedImage,
                    isCapturing = editIssueUiState.isCapturing,
                    onRetakeClicked = {
                        editIssueViewModel.handleIntent(EditIssueIntent.OnRetakeClicked)
                    },
                    onConfirmClicked = {
                        editIssueViewModel.handleIntent(EditIssueIntent.OnSubmitClicked)
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
                        editIssueViewModel.handleIntent(
                            EditIssueIntent.OnSaveClicked(
                                title = titleState.text.toString(),
                                notes = notesState.text.toString(),
                                location = locationState.text.toString(),
                                priority = selectedPriority,
                                status = selectedStatus,
                            )
                        )
                    },
                    titleError = editIssueUiState.titleError,
                    notesError = editIssueUiState.notesError,
                    locationError = editIssueUiState.locationError,
                )
            }
        }
        if (editIssueUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}