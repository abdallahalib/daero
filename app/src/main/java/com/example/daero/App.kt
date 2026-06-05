package com.example.daero

import android.app.Application
import com.example.daero.issue_list.di.databaseModule
import com.example.daero.issue_list.di.remoteServiceModule
import com.example.daero.issue_list.di.repositoryModule
import com.example.daero.issue_list.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            workManagerFactory()
            modules(
                databaseModule,
                repositoryModule,
                viewModelModule,
                remoteServiceModule,
            )
        }
    }
}