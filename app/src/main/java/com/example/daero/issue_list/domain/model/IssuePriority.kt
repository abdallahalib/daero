package com.example.daero.issue_list.domain.model

enum class IssuePriority {
    LOW,
    MEDIUM,
    HIGH;

    fun toReadableString(): String {
        return when (this) {
            LOW -> "Low"
            MEDIUM -> "Medium"
            HIGH -> "High"
        }
    }
}