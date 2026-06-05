package com.example.daero.issue_list.di

import com.example.daero.issue_list.data.worker.SyncWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker { SyncWorker(get(), get()) }
}