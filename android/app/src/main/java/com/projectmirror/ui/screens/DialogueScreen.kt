package com.projectmirror.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.projectmirror.ui.theme.MirrorColors

data class DialogueChoice(
    val id: String,
    val label: String,
)

@Composable
fun DialogueScreen(
    speaker: String,
    text: String,
    choices: List<DialogueChoice>,
    onChoice: (DialogueChoice) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Text(
            text = speaker,
            style = MaterialTheme.typography.titleSmall,
            color = MirrorColors.SpeakerName,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MirrorColors.DialogueBody,
            modifier = Modifier.padding(vertical = 16.dp),
        )
        choices.forEach { choice ->
            Button(
                onClick = { onChoice(choice) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.outlinedButtonColors(),
            ) {
                Text(choice.label)
            }
        }
    }
}
