package com.example.daero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daero.issue_list.presentation.IssueListScreen
import com.example.daero.issue_list.presentation.IssueListViewModel
import com.example.daero.new_issue.presenation.CameraPreview
import com.example.daero.ui.theme.DaeroTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val issueListViewModel: IssueListViewModel by viewModel()

    private val cameraPermissionLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            TODO("Not implemented")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
        enableEdgeToEdge()
        setContent {
            val issueListUiState = issueListViewModel.uiState.collectAsStateWithLifecycle().value
            DaeroTheme(darkTheme = true) {
                IssueListScreen(
                    issueListUiState = issueListUiState,
                    onIntent = issueListViewModel::handleIntent
                )
            }
        }
    }
}