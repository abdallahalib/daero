package com.example.daero.edit_issue

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class EditIssueUiState(
    val surfaceRequest: SurfaceRequest? = null,
    val isLoading: Boolean = false,
    val isCapturing: Boolean = false,
    val capturedImage: String? = null,
    val titleError: String? = null,
    val notesError: String? = null,
    val locationError: String? = null,
    val currentPage: Int = 0,
    val title: String = "",
    val notes: String = "",
    val location: String = "",
    val priority: IssuePriority = IssuePriority.MEDIUM,
    val status: IssueStatus = IssueStatus.OPEN,
    val createdAt: Long? = null,
    val errorMessage: String? = null,
)

sealed class EditIssueIntent {
    object OnBackClicked : EditIssueIntent()
    object OnCapturePhotoClicked : EditIssueIntent()

    object OnRetakeClicked : EditIssueIntent()

    object OnSubmitClicked : EditIssueIntent()

    class OnSaveClicked(
        val title: String,
        val notes: String,
        val location: String,
        val priority: IssuePriority,
        val status: IssueStatus,
    ) : EditIssueIntent()
}

sealed class EditIssueEffect {
    object NavigateBack : EditIssueEffect()
}

class EditIssueViewModel(
    private val issueId: String,
    private val appStorage: AppStorage,
    private val issueListRepository: IssueListRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditIssueUiState())
    val uiState: StateFlow<EditIssueUiState> = issueListRepository.loadIssueById(issueId)
        .map { result ->
            when (result) {
                is Result.Success -> EditIssueUiState(
                    isLoading = false,
                    capturedImage = result.data.photoPath,
                    title = result.data.title,
                    notes = result.data.notes,
                    location = result.data.location,
                    priority = result.data.priority,
                    status = result.data.status,
                    createdAt = result.data.createdAt,
                )

                is Result.Error -> EditIssueUiState(
                    isLoading = false,
                    errorMessage = result.message,
                )

                Result.Loading -> EditIssueUiState(isLoading = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EditIssueUiState(),
        )

    private val _effect = Channel<EditIssueEffect>(Channel.BUFFERED)
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

    fun handleIntent(intent: EditIssueIntent) {
        when (intent) {
            is EditIssueIntent.OnBackClicked -> {
                if (_uiState.value.currentPage == 0) {
                    _effect.trySend(EditIssueEffect.NavigateBack)
                } else {
                    _uiState.update {
                        it.copy(
                            currentPage = it.currentPage - 1
                        )
                    }
                }
            }

            is EditIssueIntent.OnCapturePhotoClicked -> {
                takePhoto()
            }

            is EditIssueIntent.OnRetakeClicked -> {
                _uiState.update {
                    it.copy(
                        capturedImage = null
                    )
                }
            }

            EditIssueIntent.OnSubmitClicked -> {
                submitPhoto()
            }

            is EditIssueIntent.OnSaveClicked -> {
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
        val titleError = if (title.isBlank()) "Title cannot be empty" else null
        val notesError = if (notes.isBlank()) "Notes cannot be empty" else null
        val locationError = if (location.isBlank()) "Location cannot be empty" else null
        _uiState.update {
            it.copy(
                titleError = titleError,
                notesError = notesError,
                locationError = locationError,
            )
        }
        if (titleError != null || notesError != null || locationError != null) {
            return
        }
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val issue = Issue(
                id = issueId,
                title = title,
                notes = notes,
                location = location,
                priority = priority,
                status = status,
                syncStatus = IssueSyncStatus.PENDING,
                photoPath = _uiState.value.capturedImage ?: return@launch,
                createdAt = _uiState.value.createdAt ?: return@launch,
                updatedAt = System.currentTimeMillis(),
            )
            val result = issueListRepository.updateIssue(issue)
            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.send(EditIssueEffect.NavigateBack)
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
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
                val result = issueListRepository.updateIssuePhotoPath(
                    id = issueId,
                    photoPath = _uiState.value.capturedImage ?: return@launch
                )
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                currentPage = 1,
                                isLoading = false
                            )
                        }
                    }

                    else -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
        }
    }
}