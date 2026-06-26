package com.projectmirror.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MirrorColorScheme = darkColorScheme(
    primary = Color(0xFF4A6B8A),
    onPrimary = Color(0xFFE8E6E3),
    background = Color(0xFF0D0D0F),
    onBackground = Color(0xFFC8C6C3),
    surface = Color(0xFF1A1A1D),
    onSurface = Color(0xFFC8C6C3),
)

@Composable
fun MirrorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MirrorColorScheme,
        content = content,
    )
}
