package com.example.daero.issue_list.di

import com.example.daero.issue_list.data.repository.IssueListRepositoryImpl
import com.example.daero.issue_list.domain.repository.IssueListRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<IssueListRepository> { IssueListRepositoryImpl(get()) }
}