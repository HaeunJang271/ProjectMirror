package com.projectmirror.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.projectmirror.domain.model.PlayerSettings

private val MirrorColorScheme = darkColorScheme(
    primary = Color(0xFF4A6B8A),
    onPrimary = Color(0xFFE8E6E3),
    background = Color(0xFF0D0D0F),
    onBackground = Color(0xFFC8C6C3),
    surface = Color(0xFF1A1A1D),
    onSurface = Color(0xFFC8C6C3),
)

private val HighContrastColorScheme = darkColorScheme(
    primary = Color(0xFF8AB4E8),
    onPrimary = Color(0xFF000000),
    background = Color(0xFF000000),
    onBackground = Color(0xFFF0F0F0),
    surface = Color(0xFF0A0A0A),
    onSurface = Color(0xFFF0F0F0),
)

@Composable
fun MirrorTheme(
    settings: PlayerSettings = PlayerSettings(),
    content: @Composable () -> Unit,
) {
    val scheme = if (settings.highContrast) HighContrastColorScheme else MirrorColorScheme
    CompositionLocalProvider(LocalMirrorSettings provides settings) {
        MaterialTheme(
            colorScheme = scheme,
            content = content,
        )
    }
}
