package com.example.daero.issue_list.di

import com.example.daero.issue_list.data.remote.FakeRemoteService
import com.example.daero.issue_list.domain.remote.RemoteService
import org.koin.dsl.module


val remoteServiceModule = module {
    single<RemoteService> { FakeRemoteService() }
}