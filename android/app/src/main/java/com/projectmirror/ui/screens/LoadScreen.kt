package com.projectmirror.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.projectmirror.domain.model.SaveSlotInfo
import com.projectmirror.ui.screens.load.LoadViewModel
import com.projectmirror.ui.theme.MirrorColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LoadScreen(
    onBack: () -> Unit,
    onLoad: (Int) -> Unit,
    viewModel: LoadViewModel = hiltViewModel(),
) {
    val slots by viewModel.slots.collectAsStateWithLifecycle()

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
            text = "불러오기",
            style = MaterialTheme.typography.titleMedium,
            color = MirrorColors.DialogueBody,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        if (slots.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "저장된 슬롯이 없다.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MirrorColors.Narration,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(slots, key = { it.slot }) { info ->
                    LoadSlotRow(info = info, onClick = { onLoad(info.slot) })
                }
            }
        }
    }
}

@Composable
private fun LoadSlotRow(info: SaveSlotInfo, onClick: () -> Unit) {
    val date = SimpleDateFormat("M/d HH:mm", Locale.KOREA).format(Date(info.updatedAt))
    Text(
        text = "슬롯 ${info.slot}  ·  ${info.label}\n$date",
        style = MaterialTheme.typography.bodyLarge,
        color = MirrorColors.DialogueBody,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
    )
}
