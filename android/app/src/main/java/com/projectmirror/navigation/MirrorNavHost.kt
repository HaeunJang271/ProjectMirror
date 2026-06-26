package com.projectmirror.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.projectmirror.ui.screens.NarrativeScreen
import com.projectmirror.ui.screens.TitleScreen
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
                        navController.navigate(Routes.narrative("prologue", "p01")) {
                            popUpTo(Routes.TITLE)
                        }
                    }
                },
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

            LaunchedEffect(backStackEntry.id) {
                viewModel.navEvents.collect { event ->
                    navController.navigate(Routes.narrative(event.chapterId, event.sceneId)) {
                        popUpTo(Routes.TITLE) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }

            NarrativeScreen(
                state = state,
                onAdvance = { viewModel.advance() },
                onChoice = { viewModel.selectChoice(it) },
                onExit = { viewModel.selectExit(it) },
            )
        }
    }
}
