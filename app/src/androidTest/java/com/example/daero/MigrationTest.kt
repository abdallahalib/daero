package com.example.daero

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.daero.issue_list.data.local.db.AppDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    companion object {
        private const val TEST_DB = "migration-test"
        private const val SAMPLE_ID = "Test"
    }

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        listOf(),
        FrameworkSQLiteOpenHelperFactory()
    )

    private fun SupportSQLiteDatabase.insertV1Issue(
        id: String = SAMPLE_ID,
        photoPath: String = "Test",
        title: String = "Test",
        notes: String = "Test",
        location: String = "Test",
        status: String = "OPEN",
        priority: String = "LOW",
        createdAt: Long = 1700000000L,
        updatedAt: Long = 1700000000L,
        syncStatus: String = "SYNCED"
    ) {
        execSQL(
            "INSERT INTO issues (id, photo_path, title, notes, location, status, priority, created_at, updated_at, sync_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            arrayOf(id, photoPath, title, notes, location, status, priority, createdAt, updatedAt, syncStatus)
        )
    }

    private fun SupportSQLiteDatabase.insertV2Issue(
        id: String = SAMPLE_ID,
        photoPath: String = "Test",
        title: String = "Test",
        notes: String = "Test",
        location: String = "Test",
        status: String = "OPEN",
        priority: String = "LOW",
        createdAt: Long = 1700000000L,
        updatedAt: Long = 1700000000L,
        syncStatus: String = "SYNCED",
        isDraft: Int = 0
    ) {
        execSQL(
            "INSERT INTO issues (id, photo_path, title, notes, location, status, priority, created_at, updated_at, sync_status, is_draft) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            arrayOf(id, photoPath, title, notes, location, status, priority, createdAt, updatedAt, syncStatus, isDraft)
        )
    }

    // ─── migration 1 to 2 ─────────────────────────────────────────────────────

    @Test
    fun migrate1To2_columnExists_andDefaultsToFalse() {
        val db1 = helper.createDatabase(TEST_DB, 1)
        db1.insertV1Issue()
        db1.close()

        val db = helper.runMigrationsAndValidate(
            TEST_DB, 2, true, AppDatabase.MIGRATION_1_2
        )

        val cursor = db.query("SELECT * FROM issues WHERE id = '$SAMPLE_ID'")
        assertTrue(cursor.moveToFirst())
        val colIndex = cursor.getColumnIndex("is_draft")
        assertTrue(colIndex > -1)
        assertEquals(0, cursor.getInt(colIndex))
        cursor.close()
    }

    @Test
    fun migrate1To2_multipleRows_allDefaultToNotDraft() {
        val db1 = helper.createDatabase(TEST_DB, 1)
        db1.insertV1Issue(id = "issue-1")
        db1.insertV1Issue(id = "issue-2")
        db1.insertV1Issue(id = "issue-3")
        db1.close()

        val db = helper.runMigrationsAndValidate(
            TEST_DB, 2, true, AppDatabase.MIGRATION_1_2
        )

        val cursor = db.query("SELECT is_draft FROM issues")
        assertEquals(3, cursor.count)
        while (cursor.moveToNext()) {
            assertEquals(0, cursor.getInt(cursor.getColumnIndexOrThrow("is_draft")))
        }
        cursor.close()
    }

    @Test
    fun migrate2To3_remoteIdColumnExists() {
        val db2 = helper.createDatabase(TEST_DB, 2)
        db2.insertV2Issue()
        db2.close()

        val db = helper.runMigrationsAndValidate(
            TEST_DB, 3, true, AppDatabase.MIGRATION_2_3
        )

        val cursor = db.query("SELECT * FROM issues WHERE id = '$SAMPLE_ID'")
        assertTrue(cursor.moveToFirst())
        val colIndex = cursor.getColumnIndex("remote_id")
        assertTrue(colIndex > -1)
        cursor.close()
    }

    @Test
    fun migrate2To3_existingRows_remoteIdDefaultsToNull() {
        val db2 = helper.createDatabase(TEST_DB, 2)
        db2.insertV2Issue(id = "issue-1")
        db2.insertV2Issue(id = "issue-2")
        db2.close()

        val db = helper.runMigrationsAndValidate(
            TEST_DB, 3, true, AppDatabase.MIGRATION_2_3
        )

        val cursor = db.query("SELECT remote_id FROM issues")
        assertEquals(2, cursor.count)
        while (cursor.moveToNext()) {
            assertTrue(cursor.isNull(cursor.getColumnIndexOrThrow("remote_id")))
        }
        cursor.close()
    }

    @Test
    fun migrate2To3_existingData_preserved() {
        val db2 = helper.createDatabase(TEST_DB, 2)
        db2.insertV2Issue(id = "issue-1", title = "Important field note")
        db2.close()

        val db = helper.runMigrationsAndValidate(
            TEST_DB, 3, true, AppDatabase.MIGRATION_2_3
        )

        val cursor = db.query("SELECT title FROM issues WHERE id = 'issue-1'")
        assertTrue(cursor.moveToFirst())
        assertEquals("Important field note", cursor.getString(cursor.getColumnIndexOrThrow("title")))
        cursor.close()
    }

    @Test
    fun migrateAllVersions_databaseOpensSuccessfully() {
        helper.createDatabase(TEST_DB, 1).close()

        val db = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
            TEST_DB
        )
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3)
            .build()
        db.openHelper.writableDatabase
        db.close()
    }
}