package com.example.daero.issue_list.data.repository

import android.util.Log
import com.example.daero.issue_list.data.local.dao.IssueDao
import com.example.daero.issue_list.data.local.entity.IssueEntity
import com.example.daero.issue_list.data.local.entity.toDomain
import com.example.daero.issue_list.data.local.entity.toEntity
import com.example.daero.issue_list.domain.model.Issue
import com.example.daero.issue_list.domain.model.IssueSyncStatus
import com.example.daero.issue_list.domain.model.Result
import com.example.daero.issue_list.domain.remote.RemoteService
import com.example.daero.issue_list.domain.repository.IssueListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val TAG = "IssueListRepositoryImpl"
class IssueListRepositoryImpl(
    private val issueDao: IssueDao,
    private val remoteService: RemoteService,
): IssueListRepository {

    override fun loadAllIssues(): Flow<Result<List<Issue>>> {
        return issueDao.loadAllIssues().map<List<IssueEntity>, Result<List<Issue>>> { issueList ->
            Result.Success(issueList.map { it.toDomain() })
        }.catch {
            emit(Result.Error(it, "Failed to load issues"))
        }
    }

    override fun loadIssueById(id: String): Flow<Result<Issue>> {
        return issueDao.loadIssueById(id).map { issue ->
            issue?.let { Result.Success(it.toDomain()) }
                ?: Result.Error(NoSuchElementException("Issue not found"), "Issue not found")
        }.catch {
            emit(Result.Error(it, "Failed to load issue"))
        }
    }

    override suspend fun insertIssue(issue: Issue): Result<Unit> {
        return try {
            issueDao.upsertIssue(issue.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to insert issue")
        }
    }

    override suspend fun updateIssue(issue: Issue): Result<Unit> {
        return try {
            issueDao.updateIssue(issue.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to update issue")
        }
    }

    override suspend fun updateIssueSyncStatus(id: String, syncStatus: IssueSyncStatus): Result<Unit> {
        return try {
            issueDao.updateIssueSyncStatus(id, syncStatus)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to update issue sync status")
        }
    }

    override suspend fun updateIssuePhotoPath(id: String, photoPath: String): Result<Unit> {
        return try {
            issueDao.updateIssuePhotoPath(id, photoPath)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to update issue photo path")
        }
    }

    override suspend fun syncIssues(ids: List<String>): Result<Unit> {
        Log.d(TAG, "Syncing issues")
        return try {
            ids.forEach { id ->
                val issue = issueDao.loadIssueById(id).first() ?: return@forEach
                if (issue.syncStatus == IssueSyncStatus.SYNCED) {
                    Log.d(TAG, "Issue with id $id is already synced")
                    return@forEach
                } else if (issue.syncStatus != IssueSyncStatus.PENDING) {
                    issueDao.updateIssueSyncStatus(id, IssueSyncStatus.PENDING)
                }
                Log.d(TAG, "Syncing issue with id $id")
                issueDao.updateIssue(remoteService.createIssue(issue.toDomain()).toEntity())
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to sync issues")
        }
    }
}