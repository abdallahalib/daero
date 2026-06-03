package com.example.daero.new_issue.presenation

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.awaitCancellation

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    preview: Preview,
    surfaceRequest: SurfaceRequest?,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(preview, lifecycleOwner) {
        val provider = ProcessCameraProvider.awaitInstance(context)
        try {
            provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview)
            awaitCancellation()
        } finally {
            provider.unbind(preview)
        }
    }

    surfaceRequest?.let { request: SurfaceRequest ->
        CameraXViewfinder(
            surfaceRequest = request,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alignment = Alignment.TopCenter
        )
    }
}