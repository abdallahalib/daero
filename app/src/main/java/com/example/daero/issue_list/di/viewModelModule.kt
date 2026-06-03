package com.example.daero.issue_list.di

import com.example.daero.issue_list.presentation.IssueListViewModel
import com.example.daero.new_issue.NewIssueViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        IssueListViewModel(get())
    }
    viewModel {
        NewIssueViewModel()
    }
}