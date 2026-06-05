package com.example.daero.issue_list.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.daero.issue_list.data.local.entity.IssueEntity
import com.example.daero.issue_list.domain.model.IssueSyncStatus
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.ls.LSInput

@Dao
interface IssueDao {

    @Query("SELECT * FROM issues")
    fun loadAllIssues(): Flow<List<IssueEntity>>

    @Query("SELECT * FROM issues WHERE id = :id LIMIT 1")
    fun loadIssueById(id: String): Flow<IssueEntity?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIssue(issue: IssueEntity)

    @Update
    suspend fun updateIssue(issue: IssueEntity)

    @Upsert
    suspend fun upsertIssue(issue: IssueEntity)

    @Query("UPDATE issues SET sync_status = :syncStatus WHERE id = :id")
    suspend fun updateIssueSyncStatus(id: String, syncStatus: IssueSyncStatus)

    @Query("UPDATE issues SET photo_path = :photoPath WHERE id = :id")
    suspend fun updateIssuePhotoPath(id: String, photoPath: String)

    @Query("UPDATE issues SET photo_path = :photoPath, sync_status = :syncStatus WHERE id = :id")
    suspend fun updateIssuePhotoPathAndSyncStatus(id: String, photoPath: String, syncStatus: IssueSyncStatus)

    @Query("SELECT id FROM issues WHERE sync_status = 'PENDING'")
    suspend fun getAllPendingIssuesIds(): List<String>
}