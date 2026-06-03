package com.example.daero.issue_list.di

import androidx.room.Room
import com.example.daero.issue_list.data.local.db.AppDatabase
import com.example.daero.issue_list.data.repository.IssueListRepositoryImpl
import com.example.daero.issue_list.domain.repository.IssueListRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "issue_database"
        ).build()
    }

    single { get<AppDatabase>().issueDao() }
}