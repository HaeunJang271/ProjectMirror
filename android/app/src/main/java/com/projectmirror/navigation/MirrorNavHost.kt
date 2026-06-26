package com.projectmirror.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.projectmirror.ui.components.AmbientBackground
import com.projectmirror.ui.screens.JournalScreen
import com.projectmirror.ui.screens.NarrativeScreen
import com.projectmirror.ui.screens.PauseScreen
import com.projectmirror.ui.screens.SettingsScreen
import com.projectmirror.ui.screens.TitleScreen
import com.projectmirror.ui.screens.narrative.NarrativeViewModel
import com.projectmirror.ui.screens.title.TitleViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun MirrorNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.TITLE,
    ) {
        composable(Routes.TITLE) {
            val viewModel: TitleViewModel = hiltViewModel()
            TitleScreen(
                onNewGame = {
                    viewModel.startNewGame {
                        navController.navigate(Routes.narrative("prologue", "p01")) {
                            popUpTo(Routes.TITLE) { inclusive = true }
                        }
                    }
                },
                onContinue = {
                    viewModel.continueGame { progress ->
                        navController.navigate(
                            Routes.narrative(progress.chapterId, progress.sceneId),
                        ) {
                            popUpTo(Routes.TITLE) { inclusive = true }
                        }
                    }
                },
                onSettings = { navController.navigate(Routes.SETTINGS) },
            )
        }

        composable(
            route = Routes.NARRATIVE,
            arguments = listOf(
                navArgument(Routes.CHAPTER_ID) { type = NavType.StringType },
                navArgument(Routes.SCENE_ID) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val viewModel: NarrativeViewModel = hiltViewModel(backStackEntry)
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            BackHandler {
                navController.navigate(Routes.PAUSE)
            }

            LaunchedEffect(backStackEntry.id) {
                viewModel.navEvents.collect { event ->
                    navController.navigate(Routes.narrative(event.chapterId, event.sceneId)) {
                        popUpTo(Routes.TITLE) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }

            AmbientBackground(ambientShift = state.ambientShift) {
                NarrativeScreen(
                    state = state,
                    onAdvance = { viewModel.advance() },
                    onChoice = { viewModel.selectChoice(it) },
                    onExit = { viewModel.selectExit(it) },
                    onOpenJournal = { navController.navigate(Routes.JOURNAL) },
                )
            }
        }

        composable(Routes.PAUSE) {
            PauseScreen(
                onResume = { navController.popBackStack() },
                onJournal = {
                    navController.popBackStack()
                    navController.navigate(Routes.JOURNAL)
                },
                onSettings = {
                    navController.popBackStack()
                    navController.navigate(Routes.SETTINGS)
                },
                onTitle = {
                    navController.navigate(Routes.TITLE) {
                        popUpTo(Routes.TITLE) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.JOURNAL) {
            JournalScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
