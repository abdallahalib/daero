package com.example.daero.issue_list.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daero.issue_list.domain.model.Result
import com.example.daero.issue_list.domain.repository.IssueListRepository
import com.example.daero.issue_list.presentation.IssueListEffect.*
import com.example.daero.issue_list.presentation.model.IssueUi
import com.example.daero.issue_list.presentation.model.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class IssueListUiState(
    val issues: List<IssueUi> = emptyList(),
    val isLoading: Boolean = false,
)

sealed class IssueListIntent {
    data object OnAddIssueClicked : IssueListIntent()
    data class OnIssueClicked(val issueId: String, val isDraft: Boolean) : IssueListIntent()

    data object OnSyncClicked : IssueListIntent()
}

sealed class IssueListEffect {
    data object NavigateToNewIssueScreen : IssueListEffect()
    data class NavigateToIssueDetailScreen(val issueId: String) : IssueListEffect()

    data class NavigateToEditIssueScreen(val issueId: String) : IssueListEffect()
}

private const val TAG = "IssueListViewModel"
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

    private val _effect = Channel<IssueListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()
    fun handleIntent(intent: IssueListIntent) {
        when (intent) {
            is IssueListIntent.OnAddIssueClicked -> {
                _effect.trySend(IssueListEffect.NavigateToNewIssueScreen)
            }

            is IssueListIntent.OnIssueClicked -> {
                if (intent.isDraft) {
                    _effect.trySend(NavigateToEditIssueScreen(intent.issueId))
                } else {
                    _effect.trySend(NavigateToIssueDetailScreen(intent.issueId))
                }
            }

            IssueListIntent.OnSyncClicked -> {
                syncIssues()
            }
        }
    }

    private fun syncIssues() {
        viewModelScope.launch {
            val result = repository.syncIssues(_uiState.value.issues.map { it.id })
            if (result is Result.Success) {
                Log.d(TAG, "Issues synced successfully")
            } else {
                Log.e(TAG, "Failed to sync issues: ${(result as? Result.Error)?.message}")
            }
        }
    }
}