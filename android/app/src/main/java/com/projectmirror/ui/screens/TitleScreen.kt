package com.projectmirror.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projectmirror.ui.screens.title.TitleViewModel
import com.projectmirror.ui.theme.MirrorColors

@Composable
fun TitleScreen(
    onNewGame: () -> Unit,
    onContinue: () -> Unit,
    onSettings: () -> Unit,
    viewModel: TitleViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Project MIRROR",
            style = MaterialTheme.typography.headlineMedium,
            color = MirrorColors.DialogueBody,
        )

        TitleMenuItem(
            text = "새 이야기",
            modifier = Modifier.padding(top = 48.dp),
            onClick = onNewGame,
        )

        if (state.hasProgress) {
            TitleMenuItem(
                text = state.progressLabel ?: "이어하기",
                onClick = onContinue,
            )
        }

        TitleMenuItem(
            text = "설정",
            onClick = onSettings,
        )
    }
}

@Composable
private fun TitleMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MirrorColors.Narration,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
    )
}
