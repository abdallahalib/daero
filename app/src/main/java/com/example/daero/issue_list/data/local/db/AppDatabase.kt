package com.example.daero.issue_list.data.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.daero.issue_list.data.local.dao.IssueDao
import com.example.daero.issue_list.data.local.entity.IssueEntity

@Database(
    entities = [IssueEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun issueDao(): IssueDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE issues ADD COLUMN is_draft INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
