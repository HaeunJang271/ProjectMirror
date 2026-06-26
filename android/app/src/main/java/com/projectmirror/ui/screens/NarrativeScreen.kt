package com.projectmirror.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import com.projectmirror.domain.model.ChoiceOption
import com.projectmirror.domain.model.SceneExit
import com.projectmirror.ui.components.NarrativeLineText
import com.projectmirror.ui.screens.narrative.NarrativePhase
import com.projectmirror.ui.screens.narrative.NarrativeUiState
import com.projectmirror.ui.theme.MirrorColors

@Composable
fun NarrativeScreen(
    state: NarrativeUiState,
    onAdvance: () -> Unit,
    onChoice: (ChoiceOption) -> Unit,
    onExit: (SceneExit) -> Unit,
    onOpenJournal: () -> Unit = {},
) {
    val showJournalButton = !state.isLoading && state.error == null &&
        state.phase != NarrativePhase.CHAPTER_CARD

    Box(Modifier.fillMaxSize()) {
        when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MirrorColors.SpeakerName)
            }
        }
        state.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error, color = MaterialTheme.colorScheme.error)
            }
        }
        state.phase == NarrativePhase.CHAPTER_CARD -> {
            val onAdvanceState by rememberUpdatedState(onAdvance)
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.chapterCardTitle.orEmpty(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MirrorColors.DialogueBody,
                        textAlign = TextAlign.Center,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onAdvanceState,
                        ),
                )
            }
        }
        state.phase == NarrativePhase.CHOICES -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                state.displayLines.lastOrNull()?.let { last ->
                    NarrativeLineText(
                        line = last,
                        sceneSpeaker = state.speaker,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }
                state.choices.forEach { choice ->
                    Button(
                        onClick = { onChoice(choice) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MirrorColors.ChoiceLabel,
                        ),
                    ) {
                        Text(choice.label)
                    }
                }
            }
        }
        state.phase == NarrativePhase.HUB_EXITS -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                state.displayLines.forEach { line ->
                    NarrativeLineText(
                        line = line,
                        sceneSpeaker = state.speaker,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                }
                state.exits.forEach { exit ->
                    Button(
                        onClick = { onExit(exit) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MirrorColors.ChoiceLabel,
                        ),
                    ) {
                        Text(exit.label)
                    }
                }
            }
        }
        else -> {
            val onAdvanceState by rememberUpdatedState(onAdvance)
            val tapInteraction = remember { MutableInteractionSource() }
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 48.dp),
                ) {
                    state.currentLine?.let { line ->
                        NarrativeLineText(
                            line = line,
                            sceneSpeaker = state.speaker,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                        .clickable(
                            interactionSource = tapInteraction,
                            indication = null,
                            onClick = onAdvanceState,
                        ),
                )
            }
        }
        }

        if (showJournalButton) {
            Text(
                text = "일기",
                style = MaterialTheme.typography.labelLarge,
                color = MirrorColors.Narration,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(2f)
                    .clickable(onClick = onOpenJournal)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
            )
        }
    }
}
