package com.example.daero.new_issue

import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

data class NewIssueUiState(
    val surfaceRequest: SurfaceRequest? = null
)

sealed class NewIssueIntent {
    object OnBackClicked : NewIssueIntent()
}

sealed class NewIssueEffect {
    object NavigateBack : NewIssueEffect()
}

class NewIssueViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewIssueUiState())
    val uiState: StateFlow<NewIssueUiState> = _uiState

    private val _effect = Channel<NewIssueEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    val preview: Preview =
        Preview.Builder().build().apply {
            setSurfaceProvider { request ->
                _uiState.update { it.copy(surfaceRequest = request) }
            }
        }

    fun handleIntent(intent: NewIssueIntent) {
        when (intent) {
            is NewIssueIntent.OnBackClicked -> {
                _effect.trySend(NewIssueEffect.NavigateBack)
            }
        }
    }
}