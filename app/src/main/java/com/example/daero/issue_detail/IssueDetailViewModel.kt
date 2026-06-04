package com.example.daero.issue_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daero.issue_list.domain.model.Result
import com.example.daero.issue_list.domain.repository.IssueListRepository
import com.example.daero.issue_list.presentation.model.IssueUi
import com.example.daero.issue_list.presentation.model.toUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class IssueDetailUiState(
    val isLoading: Boolean = true,
    val issue: IssueUi? = null,
    val errorMessage: String? = null,
)

class IssueDetailViewModel(
    issueId: String,
    repository: IssueListRepository,
) : ViewModel() {

    val uiState: StateFlow<IssueDetailUiState> = repository.loadIssueById(issueId)
        .map { result ->
            when (result) {
                is Result.Success -> IssueDetailUiState(
                    isLoading = false,
                    issue = result.data.toUi(),
                )

                is Result.Error -> IssueDetailUiState(
                    isLoading = false,
                    errorMessage = result.message,
                )

                Result.Loading -> IssueDetailUiState(isLoading = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = IssueDetailUiState(),
        )
}

