package com.example.daero.issue_list.presentation.model

import com.example.daero.issue_list.domain.model.Issue
import com.example.daero.issue_list.presentation.util.toReadableDateTime

data class IssueUi(
    val id: String,
    val photoPath: String,
    val title: String,
    val notes: String,
    val location: String,
    val priority: String,
    val status: String,
    val syncStatus: String,
    val createdAt: String,
    val updatedAt: String,
    val isDraft: Boolean = false
)

fun Issue.toUi(): IssueUi {
    return IssueUi(
        id = id,
        photoPath = photoPath,
        title = title,
        notes = notes,
        location = location,
        priority = priority.toReadableString(),
        status = status.toReadableString(),
        syncStatus = syncStatus.toReadableString(),
        createdAt = createdAt.toReadableDateTime(),
        updatedAt = updatedAt.toReadableDateTime(),
        isDraft = isDraft
    )
}