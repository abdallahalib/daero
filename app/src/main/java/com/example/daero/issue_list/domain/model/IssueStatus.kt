package com.example.daero.issue_list.domain.model

enum class IssueStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED;

    fun toReadableString(): String {
        return when (this) {
            OPEN -> "Open"
            IN_PROGRESS -> "In Progress"
            RESOLVED -> "Resolved"
        }
    }
}