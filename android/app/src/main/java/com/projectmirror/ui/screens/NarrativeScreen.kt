package com.projectmirror.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.projectmirror.domain.model.NarrativeLine

@Composable
fun NarrativeScreen(
    line: NarrativeLine?,
    isLoading: Boolean,
    error: String?,
    onAdvance: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = !isLoading && error == null, onClick = onAdvance)
            .padding(horizontal = 28.dp, vertical = 48.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(text = error, color = MaterialTheme.colorScheme.error)
            line != null -> {
                val prefix = when (line.type) {
                    "narration" -> ""
                    else -> line.speaker?.let { "$it\n" } ?: ""
                }
                Text(
                    text = prefix + line.text,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
