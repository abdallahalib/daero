package com.example.daero.issue_list.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.daero.issue_list.presentation.model.IssueUi

@Composable
fun IssueItem(
    issueUi: IssueUi,
    onCLick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onCLick
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = issueUi.photoPath,
            contentDescription = issueUi.title,
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
        )
        if (issueUi.isDraft) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Draft", color = MaterialTheme.colorScheme.error)
                Text(text = issueUi.createdAt)
            }
        } else {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "${issueUi.title} (${issueUi.status})")
                Text(text = "Priority: ${issueUi.priority}")
                Text(text = issueUi.createdAt)
            }
            Text(text = issueUi.syncStatus)
        }
    }
}

@Preview
@Composable
fun IssueItemPreview() {
    val issueUi = IssueUi(
        id = "1",
        photoPath = "",
        title = "Test Title",
        notes = "Test Notes",
        location = "Test Location",
        priority = "High",
        status = "Open",
        syncStatus = "Synced",
        createdAt = "2024-06-01 12:00",
        updatedAt = "2024-06-01 12:00",
        isDraft = true
    )
    IssueItem(issueUi = issueUi, onCLick = {})
}