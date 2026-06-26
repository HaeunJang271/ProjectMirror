package com.projectmirror.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.projectmirror.ui.theme.MirrorColors

@Composable
fun PauseScreen(
    onResume: () -> Unit,
    onJournal: () -> Unit,
    onSave: () -> Unit,
    onSettings: () -> Unit,
    onTitle: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "—",
            style = MaterialTheme.typography.headlineSmall,
            color = MirrorColors.Narration,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        PauseMenuItem("돌아가기", onResume)
        PauseMenuItem("일기", onJournal)
        PauseMenuItem("저장", onSave)
        PauseMenuItem("설정", onSettings)
        PauseMenuItem("타이틀로", onTitle)
    }
}

@Composable
private fun PauseMenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MirrorColors.DialogueBody,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
    )
}
