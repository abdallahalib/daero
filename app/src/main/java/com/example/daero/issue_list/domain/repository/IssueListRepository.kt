package com.example.daero.issue_list.domain.repository

import com.example.daero.issue_list.domain.model.Issue
import com.example.daero.issue_list.domain.model.IssueSyncStatus
import com.example.daero.issue_list.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface IssueListRepository {
    fun loadAllIssues(): Flow<Result<List<Issue>>>
    fun loadIssueById(id: String): Flow<Result<Issue>>
    suspend fun insertIssue(issue: Issue): Result<Unit>
    suspend fun updateIssue(issue: Issue): Result<Unit>
    suspend fun updateIssueSyncStatus(id: String, syncStatus: IssueSyncStatus): Result<Unit>

    suspend fun updateIssuePhotoPath(id: String, photoPath: String): Result<Unit>

    suspend fun syncIssues(issues: List<Issue>): Result<Unit>
}