package com.example.daero.issue_list.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.daero.R
import com.example.daero.issue_list.presentation.components.IssueList
import com.example.daero.issue_list.presentation.model.IssueUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueListScreen(
    issueListUiState: IssueListUiState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Field Notes")
                },
                actions = {
                    IconButton(
                        onClick = { TODO("Not implemented") }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.cloud_sync_24px),
                            contentDescription = "Sync"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { TODO("Not implemented") }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.photo_camera_24px),
                    contentDescription = "Add Issue"
                )
            }
        }
    ) { paddingValues ->
        IssueList(
            modifier = Modifier.padding(paddingValues),
            issues = issueListUiState.issues,
            onItemClick = { TODO("Not implemented") }
        )
        if (issueListUiState.isLoading) {
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
fun IssueListScreenPreview() {
    val issueListUiState = IssueListUiState(
        isLoading = true,
        issues = listOf(
            IssueUi(
                id = "1",
                photoPath = "",
                title = "Test Title 1",
                notes = "Test Notes 1",
                location = "Test Location 1",
                priority = "High",
                status = "Open",
                syncStatus = "Synced",
                createdAt = "2024-06-01 12:00:00",
                updatedAt = "2024-06-01 12:00:00",
            ),
            IssueUi(
                id = "2",
                photoPath = "",
                title = "Test Title 2",
                notes = "Test Notes 2",
                location = "Test Location 2",
                priority = "Low",
                status = "Closed",
                syncStatus = "Not Synced",
                createdAt = "2024-06-01 12:00:00",
                updatedAt = "2024-06-01 12:00:00",
            )
        )
    )
    IssueListScreen(issueListUiState = issueListUiState)
}