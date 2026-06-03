package com.example.daero.new_issue.presenation

import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
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
    onRetakeClicked: () -> Unit,
    onConfirmClicked: () -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (capturedImage != null) {
                    IconButton(
                        onClick = onRetakeClicked
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = com.example.daero.R.drawable.refresh_24px),
                            contentDescription = "Retake"
                        )
                    }
                }
                if (!isCapturing && capturedImage == null) {
                    CaptureButton(onClick = onCaptureClicked)
                }
                if (capturedImage != null) {
                    IconButton(
                        onClick = onConfirmClicked,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = com.example.daero.R.drawable.check_24px),
                            contentDescription = "Confirm"
                        )
                    }
                }
            }
        }
    }
}