package com.projectmirror.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projectmirror.ui.screens.save.SaveViewModel
import com.projectmirror.ui.theme.MirrorColors

@Composable
fun SaveScreen(
    onBack: () -> Unit,
    viewModel: SaveViewModel = hiltViewModel(),
) {
    val savedSlot by viewModel.savedSlot.collectAsStateWithLifecycle()

    LaunchedEffect(savedSlot) {
        if (savedSlot != null) {
            kotlinx.coroutines.delay(800)
            viewModel.clearSavedMessage()
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "← 돌아가기",
            style = MaterialTheme.typography.labelLarge,
            color = MirrorColors.Narration,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onBack)
                .padding(bottom = 16.dp),
        )
        Text(
            text = "저장",
            style = MaterialTheme.typography.titleMedium,
            color = MirrorColors.DialogueBody,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )

        if (savedSlot != null) {
            Text(
                text = "슬롯 ${savedSlot}에 저장했다.",
                style = MaterialTheme.typography.bodyLarge,
                color = MirrorColors.SpeakerName,
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                (1..3).forEach { slot ->
                    Text(
                        text = "슬롯 $slot",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MirrorColors.Narration,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable { viewModel.saveToSlot(slot) }
                            .padding(vertical = 16.dp),
                    )
                }
            }
        }
    }
}
