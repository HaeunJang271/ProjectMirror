package com.projectmirror.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.projectmirror.ui.components.NarrativeLineText
import com.projectmirror.ui.screens.journal.JournalEntry
import com.projectmirror.ui.screens.journal.JournalViewModel
import com.projectmirror.ui.theme.MirrorColors

@Composable
fun JournalScreen(
    onBack: () -> Unit,
    viewModel: JournalViewModel = hiltViewModel(),
) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        Text(
            text = "← 돌아가기",
            style = MaterialTheme.typography.labelLarge,
            color = MirrorColors.Narration,
            modifier = Modifier
                .clickable(onClick = onBack)
                .padding(bottom = 16.dp),
        )
        Text(
            text = "일기",
            style = MaterialTheme.typography.titleMedium,
            color = MirrorColors.DialogueBody,
            modifier = Modifier.padding(bottom = 20.dp),
        )

        if (entries.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "아직 기록이 없다.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MirrorColors.Narration,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                items(entries, key = { it.id }) { entry ->
                    JournalEntryRow(entry)
                }
            }
        }
    }
}

@Composable
private fun JournalEntryRow(entry: JournalEntry) {
    if (entry.isChoice) {
        Text(
            text = entry.text,
            style = MaterialTheme.typography.bodyMedium,
            color = MirrorColors.ChoiceLabel,
        )
    } else {
        val line = com.projectmirror.domain.model.NarrativeLine(
            type = if (entry.speaker != null) "dialogue" else "narration",
            speaker = entry.speaker,
            text = entry.text,
        )
        NarrativeLineText(line = line, sceneSpeaker = entry.speaker)
    }
}
