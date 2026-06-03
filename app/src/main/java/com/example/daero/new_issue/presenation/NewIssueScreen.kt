package com.example.daero.new_issue.presenation

import androidx.camera.core.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.daero.new_issue.NewIssueUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIssueScreen(
    preview: Preview,
    newIssueUiState: NewIssueUiState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create Field Note")
                }
            )
        }
    ) { paddingValues ->
        TakePhotoContent(
            modifier = Modifier.padding(paddingValues),
            preview = preview,
            surfaceRequest = newIssueUiState.surfaceRequest,
        )
    }
}