package com.example.daero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.daero.issue_list.presentation.IssueListEffect
import com.example.daero.issue_list.presentation.IssueListScreen
import com.example.daero.issue_list.presentation.IssueListViewModel
import com.example.daero.new_issue.NewIssueEffect
import com.example.daero.new_issue.NewIssueViewModel
import com.example.daero.new_issue.presenation.NewIssueScreen
import com.example.daero.ui.theme.DaeroTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val issueListViewModel: IssueListViewModel by viewModel()
    private val newIssueViewModel: NewIssueViewModel by viewModel()

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
            val newIssueUiState = newIssueViewModel.uiState.collectAsStateWithLifecycle().value
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                issueListViewModel.effect.collect {
                    when (it) {
                        is IssueListEffect.NavigateToNewIssueScreen -> {
                            navController.navigate("new_issue")
                        }
                    }

                }
            }
            LaunchedEffect(Unit) {
                newIssueViewModel.effect.collect {
                    when (it) {
                        is NewIssueEffect.NavigateBack -> {
                            navController.navigateUp()
                        }
                    }

                }
            }
            DaeroTheme(darkTheme = true) {
                NavHost(
                    navController = navController,
                    startDestination = "issue_list"
                ) {
                    composable("issue_list") {
                        IssueListScreen(
                            issueListUiState = issueListUiState,
                            onIntent = { intent ->
                                issueListViewModel.handleIntent(intent)
                            }
                        )
                    }
                    composable("new_issue") {
                        NewIssueScreen(
                            preview = newIssueViewModel.preview,
                            newIssueUiState = newIssueUiState,
                            onIntent = { intent ->
                                newIssueViewModel.handleIntent(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}