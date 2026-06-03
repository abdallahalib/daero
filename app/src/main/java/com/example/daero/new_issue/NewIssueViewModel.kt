package com.example.daero.new_issue

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class NewIssueUiState(
    val surfaceRequest: SurfaceRequest? = null,
    val isLoading: Boolean = false,
)

sealed class NewIssueIntent {
    object OnBackClicked : NewIssueIntent()
    object OnCapturePhotoClicked : NewIssueIntent()
}

sealed class NewIssueEffect {
    object NavigateBack : NewIssueEffect()
}

class NewIssueViewModel : ViewModel() {
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
        }
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }

    private fun takePhoto() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        imageCapture.takePicture(
            cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        )
    }
}