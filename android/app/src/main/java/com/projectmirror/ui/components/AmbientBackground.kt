package com.projectmirror.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AmbientBackground(
    ambientShift: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val animatedShift by animateFloatAsState(
        targetValue = ambientShift,
        animationSpec = tween(durationMillis = 500),
        label = "ambientShift",
    )

    Box(modifier = modifier.fillMaxSize()) {
        content()
        if (animatedShift > 0f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFF3A2010).copy(alpha = animatedShift * 0.35f)),
            )
        }
        if (animatedShift < 0f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFF102030).copy(alpha = -animatedShift * 0.35f)),
            )
        }
    }
}
