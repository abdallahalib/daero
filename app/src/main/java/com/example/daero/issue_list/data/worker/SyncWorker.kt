package com.example.daero.issue_list.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.daero.issue_list.domain.repository.IssueListRepository
import com.example.daero.issue_list.domain.model.Result as DomainResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val CHANNEL_ID = "sync_channel"
private const val NOTIFICATION_ID = 123

class SyncWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val repository: IssueListRepository by inject()

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())
        try {
            val pendingIdsResult = repository.getAllPendingIssuesId()
            if (pendingIdsResult !is DomainResult.Success) {
                return Result.failure()
            }
            val pendingIds = pendingIdsResult.data

            if (pendingIds.isEmpty()) {
                return Result.success()
            }

            pendingIds.forEach {
                val result = repository.syncIssue(it)
                if (result !is DomainResult.Success) {
                    return Result.failure()
                }
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Syncing field notes")
            .setContentText("Uploading pending changes...")
            .setOngoing(true)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Sync",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Field note sync progress"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val WORK_NAME = "sync_issues"
    }
}