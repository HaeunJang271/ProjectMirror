package com.projectmirror.navigation

import androidx.compose.runtime.Composable
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
import com.projectmirror.ui.screens.prologue.PrologueViewModel

@Composable
fun MirrorNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.TITLE,
    ) {
        composable(Routes.TITLE) {
            TitleScreen(
                onNewGame = {
                    navController.navigate(Routes.prologue("p01")) {
                        popUpTo(Routes.TITLE)
                    }
                },
            )
        }

        composable(
            route = Routes.PROLOGUE,
            arguments = listOf(
                navArgument(Routes.PROLOGUE_SCENE) { type = NavType.StringType },
            ),
        ) {
            val viewModel: PrologueViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            NarrativeScreen(
                line = state.currentLine,
                isLoading = state.isLoading,
                error = state.error,
                onAdvance = {
                    if (state.hasNextLine) {
                        viewModel.advance()
                    } else {
                        viewModel.nextSceneId()?.let { next ->
                            navController.navigate(Routes.prologue(next))
                        }
                    }
                },
            )
        }
    }
}
