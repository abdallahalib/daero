package com.example.daero.new_issue.presenation

import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.daero.R
import com.example.daero.issue_list.domain.model.IssuePriority
import com.example.daero.issue_list.domain.model.IssueStatus
import com.example.daero.new_issue.NewIssueIntent
import com.example.daero.new_issue.NewIssueUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIssueScreen(
    preview: Preview,
    imageCapture: ImageCapture,
    newIssueUiState: NewIssueUiState,
    onIntent: (NewIssueIntent) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val titleState = rememberTextFieldState()
    val notesState = rememberTextFieldState()
    val locationState = rememberTextFieldState()
    var selectedPriority by remember { mutableStateOf<IssuePriority>(IssuePriority.MEDIUM) }
    var selectedStatus by remember { mutableStateOf<IssueStatus>(IssueStatus.OPEN) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create Field Note")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (pagerState.canScrollBackward) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            } else {
                                onIntent(NewIssueIntent.OnBackClicked)
                            }
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
                    preview = preview,
                    surfaceRequest = newIssueUiState.surfaceRequest,
                    imageCapture = imageCapture,
                    onCaptureClicked = { onIntent(NewIssueIntent.OnCapturePhotoClicked) },
                    capturedImage = newIssueUiState.capturedImage,
                    isCapturing = newIssueUiState.isCapturing,
                    onRetakeClicked = { onIntent(NewIssueIntent.OnRetakeClicked) },
                    onConfirmClicked = {
                        onIntent(NewIssueIntent.OnSubmitClicked)
                        if (pagerState.canScrollForward) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
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