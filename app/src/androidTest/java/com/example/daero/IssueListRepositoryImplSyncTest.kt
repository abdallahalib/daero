package com.example.daero

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.daero.issue_list.data.local.dao.IssueDao
import com.example.daero.issue_list.data.local.db.AppDatabase
import com.example.daero.issue_list.data.local.entity.IssueEntity
import com.example.daero.issue_list.data.remote.FakeRemoteService
import com.example.daero.issue_list.data.repository.IssueListRepositoryImpl
import com.example.daero.issue_list.domain.model.IssuePriority
import com.example.daero.issue_list.domain.model.IssueStatus
import com.example.daero.issue_list.domain.model.IssueSyncStatus
import com.example.daero.issue_list.domain.model.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class IssueListRepositoryImplSyncTest {

    private lateinit var context: Context
    private lateinit var database: AppDatabase
    private lateinit var issueDao: IssueDao
    private lateinit var fakeRemoteService: FakeRemoteService
    private lateinit var repository: IssueListRepositoryImpl

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        issueDao = database.issueDao()
        fakeRemoteService = FakeRemoteService()
        repository = IssueListRepositoryImpl(
            workManager = WorkManager.getInstance(context),
            issueDao = issueDao,
            remoteService = fakeRemoteService
        )
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun syncIssue_success_updatesSyncStatusToSynced() = runTest {
        val issue = insertTestIssue(syncStatus = IssueSyncStatus.PENDING)

        repository.syncIssue(issue.id)

        val updated = issueDao.loadIssueById(issue.id).first()
        assertEquals(IssueSyncStatus.SYNCED, updated?.syncStatus)
    }

    @Test
    fun syncIssue_success_returnsResultSuccess() = runTest {
        val issue = insertTestIssue(syncStatus = IssueSyncStatus.PENDING)

        val result = repository.syncIssue(issue.id)

        assertTrue(result is Result.Success)
    }

    @Test
    fun syncIssue_success_preservesIssueData() = runTest {
        val issue = insertTestIssue(
            syncStatus = IssueSyncStatus.PENDING,
            title = "Important field note"
        )

        repository.syncIssue(issue.id)

        val updated = issueDao.loadIssueById(issue.id).first()
        assertEquals("Important field note", updated?.title)
    }

    @Test
    fun syncIssue_alreadySynced_skipsSync() = runTest {
        val issue = insertTestIssue(syncStatus = IssueSyncStatus.SYNCED)

        val result = repository.syncIssue(issue.id)

        val updated = issueDao.loadIssueById(issue.id).first()
        assertEquals(IssueSyncStatus.SYNCED, updated?.syncStatus)
        assertTrue(result is Result.Success)
    }

    @Test
    fun syncIssue_issueNotFound_returnsError() = runTest {
        val result = repository.syncIssue("non-existent-id")

        assertTrue(result is Result.Error)
    }

    @Test
    fun syncIssue_doesNotAffectOtherIssues() = runTest {
        val issue1 = insertTestIssue(syncStatus = IssueSyncStatus.PENDING)
        val issue2 = insertTestIssue(syncStatus = IssueSyncStatus.PENDING)

        repository.syncIssue(issue1.id)

        val updated = issueDao.loadIssueById(issue2.id).first()
        assertEquals(IssueSyncStatus.PENDING, updated?.syncStatus)
    }

    private suspend fun insertTestIssue(
        syncStatus: IssueSyncStatus = IssueSyncStatus.PENDING,
        title: String = "Test Issue"
    ): IssueEntity {
        val entity = IssueEntity(
            id = UUID.randomUUID().toString(),
            photoPath = "Test",
            title = title,
            notes = "Test",
            location = "Test",
            status = IssueStatus.OPEN,
            priority = IssuePriority.MEDIUM,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            syncStatus = syncStatus,
            isDraft = false
        )
        issueDao.upsertIssue(entity)
        return entity
    }
}