package com.example.daero.new_issue

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daero.core.storage.AppStorage
import com.example.daero.issue_list.domain.model.Issue
import com.example.daero.issue_list.domain.model.IssuePriority
import com.example.daero.issue_list.domain.model.IssueStatus
import com.example.daero.issue_list.domain.model.IssueSyncStatus
import com.example.daero.issue_list.domain.model.Result
import com.example.daero.issue_list.domain.repository.IssueListRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class NewIssueUiState(
    val surfaceRequest: SurfaceRequest? = null,
    val isLoading: Boolean = false,
    val isCapturing: Boolean = false,
    val capturedImage: String? = null,
)

sealed class NewIssueIntent {
    object OnBackClicked : NewIssueIntent()
    object OnCapturePhotoClicked : NewIssueIntent()

    object OnRetakeClicked : NewIssueIntent()

    object OnSubmitClicked : NewIssueIntent()

    class OnSaveClicked(
        val title: String,
        val notes: String,
        val location: String,
        val priority: IssuePriority,
        val status: IssueStatus,
    ) : NewIssueIntent()
}

sealed class NewIssueEffect {
    object NavigateBack : NewIssueEffect()
}

class NewIssueViewModel(
    private val appStorage: AppStorage,
    private val issueListRepository: IssueListRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewIssueUiState())
    val uiState: StateFlow<NewIssueUiState> = _uiState

    private val _effect = Channel<NewIssueEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    val preview: Preview =
        Preview.Builder().build().apply {
            setSurfaceProvider { request ->
                _uiState.update { it.copy(surfaceRequest = request) }
            }
        }

    val imageCapture: ImageCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    fun handleIntent(intent: NewIssueIntent) {
        when (intent) {
            is NewIssueIntent.OnBackClicked -> {
                _effect.trySend(NewIssueEffect.NavigateBack)
            }

            is NewIssueIntent.OnCapturePhotoClicked -> {
                takePhoto()
            }

            is NewIssueIntent.OnRetakeClicked -> {
                _uiState.update {
                    it.copy(
                        capturedImage = null
                    )
                }
            }

            NewIssueIntent.OnSubmitClicked -> {
                submitPhoto()
            }

            is NewIssueIntent.OnSaveClicked -> {
                save(
                    title = intent.title,
                    notes = intent.notes,
                    location = intent.location,
                    priority = intent.priority,
                    status = intent.status,
                )
            }
        }
    }

    private fun save(
        title: String,
        notes: String,
        location: String,
        priority: IssuePriority,
        status: IssueStatus
    ) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val issue = Issue(
                id = UUID.randomUUID().toString(),
                title = title,
                notes = notes,
                location = location,
                priority = priority,
                status = status,
                syncStatus = IssueSyncStatus.PENDING,
                photoPath = _uiState.value.capturedImage ?: return@launch,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
            )
            val result = issueListRepository.insertIssue(issue)
            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, capturedImage = null) }
                    _effect.send(NewIssueEffect.NavigateBack)
                }

                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }

    private fun takePhoto() {
        _uiState.update {
            it.copy(
                isCapturing = true
            )
        }

        val outputFile = appStorage.createImageFile()
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    _uiState.update {
                        it.copy(
                            isCapturing = false,
                            capturedImage = outputFile.absolutePath
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    _uiState.update {
                        it.copy(isCapturing = false)
                    }
                }
            }
        )
    }

    private fun submitPhoto() {
        // Add the captured image to draft
    }
}