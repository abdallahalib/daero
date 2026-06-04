package com.example.daero.issue_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daero.issue_list.domain.model.Result
import com.example.daero.issue_list.domain.repository.IssueListRepository
import com.example.daero.issue_list.presentation.model.IssueUi
import com.example.daero.issue_list.presentation.model.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

data class IssueDetailUiState(
    val isLoading: Boolean = true,
    val issue: IssueUi? = null,
    val errorMessage: String? = null,
)

sealed class IssueDetailIntent {
    object OnBackClicked : IssueDetailIntent()
    object OnEditClicked : IssueDetailIntent()
}

sealed class IssueDetailEffect {
    data object NavigateBack : IssueDetailEffect()
    data class NavigateToEditIssue(val issueId: String) : IssueDetailEffect()
}
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

    private val _effect = Channel<IssueDetailEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: IssueDetailIntent) {
        when (intent) {
            is IssueDetailIntent.OnBackClicked -> {
                _effect.trySend(IssueDetailEffect.NavigateBack)
            }

            is IssueDetailIntent.OnEditClicked -> {
                val issueId = uiState.value.issue?.id ?: return
                _effect.trySend(IssueDetailEffect.NavigateToEditIssue(issueId))
            }
        }
    }

}

