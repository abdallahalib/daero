package com.example.daero.issue_list.data.remote

import android.util.Log
import com.example.daero.issue_list.domain.model.Issue
import com.example.daero.issue_list.domain.model.IssueSyncStatus
import com.example.daero.issue_list.domain.model.Result
import com.example.daero.issue_list.domain.remote.RemoteService
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

private const val TAG = "FakeRemoteService"
class FakeRemoteService : RemoteService {

    private val issueSyncStatus = IssueSyncStatus.SYNCED

    override suspend fun createIssue(issue: Issue): Issue {
        Log.d(TAG, "Creating issue with id ${issue.id}")
        uploadPhoto(issue.id, issue.photoPath)
        return issue.copy(
            syncStatus = issueSyncStatus,
            remoteId = if (issueSyncStatus == IssueSyncStatus.SYNCED) UUID.randomUUID().toString() else null
        )
    }

    override suspend fun updateIssue(issue: Issue): Issue {
        Log.d(TAG, "Updating issue with id ${issue.id}")
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