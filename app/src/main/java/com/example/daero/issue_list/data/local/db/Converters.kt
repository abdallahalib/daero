package com.example.daero.issue_list.data.local.db

import androidx.room.TypeConverter
import com.example.daero.issue_list.domain.model.IssuePriority
import com.example.daero.issue_list.domain.model.IssueStatus
import com.example.daero.issue_list.domain.model.IssueSyncStatus

class Converters {
    @TypeConverter
    fun fromIssueStatus(value: IssueStatus): String = value.name

    @TypeConverter
    fun toIssueStatus(value: String): IssueStatus = IssueStatus.valueOf(value)

    @TypeConverter
    fun fromIssuePriority(value: IssuePriority): String = value.name

    @TypeConverter
    fun toIssuePriority(value: String): IssuePriority = IssuePriority.valueOf(value)

    @TypeConverter
    fun fromIssueSyncStatus(value: IssueSyncStatus): String = value.name

    @TypeConverter
    fun toIssueSyncStatus(value: String): IssueSyncStatus = IssueSyncStatus.valueOf(value)
}