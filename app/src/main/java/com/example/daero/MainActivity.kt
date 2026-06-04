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
import com.example.daero.issue_detail.presentation.IssueDetailScreen
import com.example.daero.issue_list.presentation.IssueListEffect
import com.example.daero.issue_list.presentation.IssueListScreen
import com.example.daero.issue_list.presentation.IssueListViewModel
import com.example.daero.navigation.IssueDetailRoute
import com.example.daero.navigation.IssueListRoute
import com.example.daero.navigation.NewIssueRoute
import com.example.daero.new_issue.presenation.NewIssueScreen
import com.example.daero.ui.theme.DaeroTheme
import androidx.navigation.toRoute
import com.example.daero.edit_issue.presentation.EditIssueScreen
import com.example.daero.navigation.EditIssueRoute
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
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                issueListViewModel.effect.collect {
                    when (it) {
                        is IssueListEffect.NavigateToNewIssueScreen -> {
                            navController.navigate(NewIssueRoute)
                        }

                        is IssueListEffect.NavigateToIssueDetailScreen -> {
                            navController.navigate(IssueDetailRoute(it.issueId))
                        }
                    }

                }
            }
            DaeroTheme(darkTheme = true) {
                NavHost(
                    navController = navController,
                    startDestination = IssueListRoute
                ) {
                    composable<IssueListRoute> {
                        IssueListScreen(
                            issueListUiState = issueListUiState,
                            onIntent = { intent ->
                                issueListViewModel.handleIntent(intent)
                            }
                        )
                    }
                    composable<NewIssueRoute> {
                        NewIssueScreen(
                            onBackClicked = { navController.navigateUp() },
                        )
                    }
                    composable<IssueDetailRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<IssueDetailRoute>()
                        IssueDetailScreen(
                            issueId = route.issueId,
                            onBackClicked = { navController.navigateUp() },
                        )
                    }
                    composable<EditIssueRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<EditIssueRoute>()
                        EditIssueScreen(
                            issueId = route.issueId,
                            onBackClicked = { navController.navigateUp() },
                        )
                    }
                }
            }
        }
    }
}