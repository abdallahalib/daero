package com.example.daero.issue_list.domain.model

data class Issue(
    val id: String,
    val photoPath: String,
    val title: String,
    val notes: String,
    val location: String,
    val priority: IssuePriority,
    val status: IssueStatus,
    val syncStatus: IssueSyncStatus,
    val createdAt: Long,
    val updatedAt: Long
)