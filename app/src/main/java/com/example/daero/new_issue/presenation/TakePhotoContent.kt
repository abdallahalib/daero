package com.example.daero.new_issue.presenation

import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TakePhotoContent(
    modifier: Modifier = Modifier,
    preview: Preview,
    surfaceRequest: SurfaceRequest?,
) {
    Box(
        modifier = modifier
    ) {
        CameraPreview(
            modifier = Modifier.align(Alignment.Center),
            preview = preview,
            surfaceRequest = surfaceRequest,
        )
    }
}