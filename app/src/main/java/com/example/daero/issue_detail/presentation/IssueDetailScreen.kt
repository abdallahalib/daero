package com.example.daero.issue_detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.daero.R
import com.example.daero.issue_detail.IssueDetailUiState
import com.example.daero.issue_detail.IssueDetailViewModel
import com.example.daero.issue_list.presentation.model.IssueUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailScreen(
    issueId: String,
    onBackClicked: () -> Unit,
    viewModel: IssueDetailViewModel = koinViewModel(parameters = { parametersOf(issueId) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    IssueDetailContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IssueDetailContent(
    uiState: IssueDetailUiState,
    onBackClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Issue Detail")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.issue == null -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.errorMessage ?: "Issue not found")
                }
            }

            else -> {
                val issue = uiState.issue
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model = issue.photoPath,
                        contentDescription = issue.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Text(text = issue.title, fontWeight = FontWeight.Bold)
                    DetailRow(label = "Status", value = issue.status)
                    DetailRow(label = "Priority", value = issue.priority)
                    DetailRow(label = "Location", value = issue.location)
                    DetailRow(label = "Sync status", value = issue.syncStatus)
                    DetailRow(label = "Created", value = issue.createdAt)
                    DetailRow(label = "Updated", value = issue.updatedAt)
                    DetailRow(label = "Notes", value = issue.notes)
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}

@Preview(showBackground = true)
@Composable
private fun IssueDetailContentPreview() {
    IssueDetailContent(
        uiState = IssueDetailUiState(
            isLoading = false,
            issue = IssueUi(
                id = "Test",
                photoPath = "Test",
                title = "Test",
                notes = "Test",
                location = "Test",
                priority = "High",
                status = "Open",
                syncStatus = "Synced",
                createdAt = "2026-06-04 09:30",
                updatedAt = "2026-06-04 09:45",
            ),
        ),
        onBackClicked = {},
    )
}

