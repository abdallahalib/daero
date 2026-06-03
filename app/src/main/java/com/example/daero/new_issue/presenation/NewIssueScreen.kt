package com.example.daero.new_issue.presenation

import androidx.camera.core.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.daero.R
import com.example.daero.new_issue.NewIssueIntent
import com.example.daero.new_issue.NewIssueUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIssueScreen(
    preview: Preview,
    newIssueUiState: NewIssueUiState,
    onIntent: (NewIssueIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create Field Note")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onIntent(NewIssueIntent.OnBackClicked) }
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
        TakePhotoContent(
            modifier = Modifier.padding(paddingValues),
            preview = preview,
            surfaceRequest = newIssueUiState.surfaceRequest,
        )
    }
}