package com.example.daero.new_issue.presenation

import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.daero.new_issue.components.CaptureButton

@Composable
fun TakePhotoContent(
    modifier: Modifier = Modifier,
    preview: Preview,
    surfaceRequest: SurfaceRequest?,
    imageCapture: ImageCapture,
    onCaptureClicked: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        CameraPreview(
            modifier = Modifier.align(Alignment.Center),
            preview = preview,
            surfaceRequest = surfaceRequest,
            imageCapture = imageCapture,
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        ) {
            CaptureButton(
                onClick = onCaptureClicked
            )
        }
    }
}