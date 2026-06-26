package com.projectmirror.ui.theme

import androidx.compose.runtime.compositionLocalOf
import com.projectmirror.domain.model.PlayerSettings

val LocalMirrorSettings = compositionLocalOf { PlayerSettings() }
