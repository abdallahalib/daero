package com.example.daero.issue_list.data.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.daero.issue_list.data.local.dao.IssueDao
import com.example.daero.issue_list.data.local.entity.IssueEntity

@Database(
    entities = [IssueEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun issueDao(): IssueDao
}
