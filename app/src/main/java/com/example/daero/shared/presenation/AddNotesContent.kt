package com.example.daero.shared.presenation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.daero.issue_list.domain.model.IssuePriority
import com.example.daero.issue_list.domain.model.IssueStatus
import com.example.daero.new_issue.components.EnumField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNotesContent(
    modifier: Modifier = Modifier,
    title: TextFieldState,
    notes: TextFieldState,
    location: TextFieldState,
    priority: IssuePriority,
    onPrioritySelected: (IssuePriority) -> Unit,
    status: IssueStatus,
    onStatusSelected: (IssueStatus) -> Unit,
    onSaveClicked: () -> Unit,
    titleError: String?,
    notesError: String?,
    locationError: String?,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            state = title,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = titleError != null,
            supportingText = { Text(titleError ?: "") }
        )
        OutlinedTextField(
            state = notes,
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            isError = notesError != null,
            supportingText = { Text(notesError ?: "") }
        )
        OutlinedTextField(
            state = location,
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth(),
            isError = locationError != null,
            supportingText = { Text(locationError ?: "") }
        )
        EnumField(
            label = "Priority",
            selectedOption = priority,
            options = IssuePriority.entries,
            onOptionSelected = onPrioritySelected,
            optionLabel = { it.toReadableString() },
        )
        EnumField(
            label = "Status",
            selectedOption = status,
            options = IssueStatus.entries,
            onOptionSelected = onStatusSelected,
            optionLabel = { it.toReadableString() },
        )
        Spacer(modifier = Modifier.weight(1f))
        FilledTonalButton(
            onClick = onSaveClicked,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save")
        }
    }
}

@Preview
@Composable
fun AddNotesContentPreview() {
    AddNotesContent(
        title = TextFieldState(""),
        notes = TextFieldState(""),
        location = TextFieldState(""),
        priority = IssuePriority.MEDIUM,
        onPrioritySelected = {},
        status = IssueStatus.OPEN,
        onStatusSelected = {},
        onSaveClicked = {},
        titleError = null,
        notesError = null,
        locationError = null,
    )
}
