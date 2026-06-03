package com.example.daero.new_issue.presenation

import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.daero.new_issue.components.CaptureButton

@Composable
fun TakePhotoContent(
    modifier: Modifier = Modifier,
    preview: Preview,
    surfaceRequest: SurfaceRequest?,
    imageCapture: ImageCapture,
    onCaptureClicked: () -> Unit,
    capturedImage: String?,
    isCapturing: Boolean,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        CameraPreview(
            modifier = Modifier.align(Alignment.Center),
            preview = preview,
            surfaceRequest = surfaceRequest,
            imageCapture = imageCapture,
            isCapturing = isCapturing,
        )
        if (capturedImage != null) {
            AsyncImage(
                model = capturedImage,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        ) {
            CaptureButton(
                onClick = onCaptureClicked
            )
        }
    }
}