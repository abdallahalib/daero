package com.example.daero.issue_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daero.issue_list.domain.model.Result
import com.example.daero.issue_list.domain.repository.IssueListRepository
import com.example.daero.issue_list.presentation.model.IssueUi
import com.example.daero.issue_list.presentation.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


data class IssueListUiState(
    val issues: List<IssueUi> = emptyList(),
    val isLoading: Boolean = false,
)

class IssueListViewModel(
    private val repository: IssueListRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(IssueListUiState())
    val uiState = combine(
        _uiState,
        repository.loadAllIssues()
    ) { uiState, issueList ->
        when (issueList) {
            is Result.Success -> {
                uiState.copy(
                    issues = issueList.data.map { it.toUi() },
                    isLoading = false,
                )
            }
            else -> {
                uiState.copy(
                    issues = emptyList(),
                    isLoading = false,
                )
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        IssueListUiState(isLoading = true)
    )
}