package com.example.daero.issue_list.di

import com.example.daero.core.storage.AppStorage
import com.example.daero.core.storage.AppStorageImpl
import com.example.daero.issue_list.presentation.IssueListViewModel
import com.example.daero.new_issue.NewIssueViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<AppStorage> {
        AppStorageImpl(androidContext())
    }
    viewModel {
        IssueListViewModel(get())
    }
    viewModel {
        NewIssueViewModel()
    }
}