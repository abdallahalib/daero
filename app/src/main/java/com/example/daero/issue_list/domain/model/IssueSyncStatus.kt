package com.example.daero.issue_list.domain.model

enum class IssueSyncStatus {
    SYNCED,
    PENDING,
    FAILED,
    CONFLICT;

    fun toReadableString(): String {
        return when (this) {
            SYNCED -> "Synced"
            PENDING -> "Pending"
            FAILED -> "Failed"
            CONFLICT -> "Conflict"
        }
    }
}