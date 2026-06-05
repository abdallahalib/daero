package com.example.daero.issue_list.data.remote

import com.example.daero.issue_list.domain.model.Issue
import com.example.daero.issue_list.domain.model.IssueSyncStatus
import com.example.daero.issue_list.domain.model.Result
import com.example.daero.issue_list.domain.remote.RemoteService
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class FakeRemoteService : RemoteService {

    private val issueSyncStatus = IssueSyncStatus.SYNCED

    override suspend fun createIssue(issue: Issue): Issue {
        uploadPhoto(issue.id, issue.photoPath)
        return issue.copy(
            syncStatus = issueSyncStatus
        )
    }

    override suspend fun updateIssue(issue: Issue): Issue {
        uploadPhoto(issue.id, issue.photoPath)
        return issue.copy(
            syncStatus = issueSyncStatus
        )
    }

    override suspend fun uploadPhoto(
        issueId: String,
        photoPath: String
    ): Result<Unit> {
        delay(5.seconds)
        return Result.Success(Unit)
    }

}