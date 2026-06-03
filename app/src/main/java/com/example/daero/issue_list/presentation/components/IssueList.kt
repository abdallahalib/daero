package com.example.daero.issue_list.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.daero.issue_list.presentation.model.IssueUi

@Composable
fun IssueList(
    modifier: Modifier = Modifier,
    issues: List<IssueUi>,
    onItemClick: (IssueUi) -> Unit,
) {
    if (issues.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(text = "No field notes yet.")
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(issues) {
                IssueItem(
                    issueUi = it,
                    onCLick = { onItemClick(it) }
                )
            }
        }
    }
}

@Preview
@Composable
fun IssueListPreview() {
    val issues = listOf(
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
            createdAt = "2024-06-02 12:00:00",
            updatedAt = "2024-06-02 12:00:00",
        )
    )
    IssueList(
        issues = issues,
        onItemClick = {}
    )
}

