package com.example.daero.issue_list.di

import androidx.work.WorkManager
import com.example.daero.issue_list.data.worker.SyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    single<WorkManager> { WorkManager.getInstance(androidContext()) }
    worker { SyncWorker(get(), get()) }
}