package com.example.daero.issue_list.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.daero.issue_list.domain.model.Issue
import com.example.daero.issue_list.domain.model.IssuePriority
import com.example.daero.issue_list.domain.model.IssueStatus
import com.example.daero.issue_list.domain.model.IssueSyncStatus

@Entity(tableName = "issues")
data class IssueEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "photo_path") val photoPath: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "status") val status: IssueStatus,
    @ColumnInfo(name = "priority") val priority: IssuePriority,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "sync_status") val syncStatus: IssueSyncStatus,
    @ColumnInfo(name = "is_draft") val isDraft: Boolean = false,
    @ColumnInfo(name = "remote_id") val remoteId: String? = null,
)

fun IssueEntity.toDomain(): Issue {
    return Issue(
        id = id,
        photoPath = photoPath,
        title = title,
        notes = notes,
        location = location,
        status = status,
        priority = priority,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncStatus = syncStatus,
        isDraft = isDraft,
        remoteId = remoteId,
    )
}

fun Issue.toEntity(): IssueEntity {
    return IssueEntity(
        id = id,
        photoPath = photoPath,
        title = title,
        notes = notes,
        location = location,
        status = status,
        priority = priority,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncStatus = syncStatus,
        isDraft = isDraft,
        remoteId = remoteId,
        )
}