package com.projectmirror.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projectmirror.domain.model.SubtitleScale
import com.projectmirror.ui.screens.settings.SettingsViewModel
import com.projectmirror.ui.theme.MirrorColors

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

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
            text = "설정",
            style = MaterialTheme.typography.titleMedium,
            color = MirrorColors.DialogueBody,
            modifier = Modifier.padding(bottom = 28.dp),
        )

        Text(
            text = "자막 크기",
            style = MaterialTheme.typography.labelLarge,
            color = MirrorColors.Narration,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SubtitleScale.entries.forEach { scale ->
                val label = when (scale) {
                    SubtitleScale.S -> "S"
                    SubtitleScale.M -> "M"
                    SubtitleScale.L -> "L"
                }
                val selected = settings.subtitleScale == scale
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selected) MirrorColors.SpeakerName else MirrorColors.Narration,
                    modifier = Modifier
                        .clickable { viewModel.setSubtitleScale(scale) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "효과음",
                style = MaterialTheme.typography.bodyLarge,
                color = MirrorColors.DialogueBody,
            )
            Switch(
                checked = settings.soundEnabled,
                onCheckedChange = viewModel::setSoundEnabled,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "고대비",
                style = MaterialTheme.typography.bodyLarge,
                color = MirrorColors.DialogueBody,
            )
            Switch(
                checked = settings.highContrast,
                onCheckedChange = viewModel::setHighContrast,
            )
        }
    }
}
