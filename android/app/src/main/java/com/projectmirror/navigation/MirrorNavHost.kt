package com.projectmirror.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projectmirror.ui.components.AmbientBackground
import com.projectmirror.ui.screens.JournalScreen
import com.projectmirror.ui.screens.LoadScreen
import com.projectmirror.ui.screens.NarrativeScreen
import com.projectmirror.ui.screens.PauseScreen
import com.projectmirror.ui.screens.SaveScreen
import com.projectmirror.ui.screens.SettingsScreen
import com.projectmirror.ui.screens.TitleScreen
import com.projectmirror.ui.screens.load.LoadViewModel
import com.projectmirror.ui.screens.narrative.NarrativeViewModel
import com.projectmirror.ui.screens.title.TitleViewModel

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
                        navController.navigate(Routes.NARRATIVE) {
                            popUpTo(Routes.TITLE) { inclusive = true }
                        }
                    }
                },
                onContinue = {
                    viewModel.continueGame {
                        navController.navigate(Routes.NARRATIVE) {
                            popUpTo(Routes.TITLE) { inclusive = true }
                        }
                    }
                },
                onLoad = { navController.navigate(Routes.LOAD) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
            )
        }

        composable(Routes.NARRATIVE) {
            val viewModel: NarrativeViewModel = hiltViewModel()
            val settingsViewModel: com.projectmirror.ui.screens.settings.SettingsViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

            androidx.compose.runtime.LaunchedEffect(settings.soundEnabled) {
                viewModel.setAudioEnabled(settings.soundEnabled)
            }

            BackHandler {
                navController.navigate(Routes.PAUSE)
            }

            AmbientBackground(ambientShift = state.ambientShift) {
                NarrativeScreen(
                    state = state,
                    onAdvance = { viewModel.advance() },
                    onChoice = { viewModel.selectChoice(it) },
                    onExit = { viewModel.selectExit(it) },
                    onOpenJournal = { navController.navigate(Routes.JOURNAL) },
                    onImplicitTouch = { viewModel.onImplicitTouch() },
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
                onSave = {
                    navController.popBackStack()
                    navController.navigate(Routes.SAVE)
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

        composable(Routes.LOAD) {
            val viewModel: LoadViewModel = hiltViewModel()
            LoadScreen(
                onBack = { navController.popBackStack() },
                onLoad = { slot ->
                    viewModel.loadSlot(slot) {
                        navController.navigate(Routes.NARRATIVE) {
                            popUpTo(Routes.TITLE) { inclusive = true }
                        }
                    }
                },
            )
        }

        composable(Routes.SAVE) {
            SaveScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.JOURNAL) {
            JournalScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
