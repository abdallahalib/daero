package com.example.daero.new_issue

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.lifecycle.ViewModel
import com.example.daero.core.storage.AppStorage
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
    val isCapturing: Boolean = false,
    val capturedImage: String? = null,
)

sealed class NewIssueIntent {
    object OnBackClicked : NewIssueIntent()
    object OnCapturePhotoClicked : NewIssueIntent()
}

sealed class NewIssueEffect {
    object NavigateBack : NewIssueEffect()
}

class NewIssueViewModel(
    private val appStorage: AppStorage,
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
}