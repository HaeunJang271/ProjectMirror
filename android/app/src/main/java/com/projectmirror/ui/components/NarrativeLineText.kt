package com.projectmirror.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.projectmirror.domain.model.NarrativeLine
import com.projectmirror.ui.theme.LocalMirrorSettings
import com.projectmirror.ui.theme.MirrorColors

@Composable
fun NarrativeLineText(
    line: NarrativeLine,
    sceneSpeaker: String? = null,
    modifier: Modifier = Modifier,
) {
    val scale = LocalMirrorSettings.current.subtitleScale.multiplier
    val isDialogue = line.type == "dialogue"
    val speaker = if (isDialogue) (line.speaker ?: sceneSpeaker) else null
    val titleStyle = MaterialTheme.typography.titleSmall.copy(
        fontSize = MaterialTheme.typography.titleSmall.fontSize * scale,
    )
    val bodyStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = MaterialTheme.typography.bodyLarge.fontSize * scale,
    )

    Column(modifier = modifier) {
        if (speaker != null) {
            Text(
                text = speaker,
                style = titleStyle,
                color = MirrorColors.SpeakerName,
            )
            Text(
                text = line.text,
                style = bodyStyle,
                color = MirrorColors.DialogueBody,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Start,
            )
        } else {
            Text(
                text = line.text,
                style = bodyStyle,
                color = MirrorColors.Narration,
                textAlign = TextAlign.Start,
            )
        }
    }
}
