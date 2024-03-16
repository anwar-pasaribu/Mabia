package ui.extension

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

@Composable
fun FadeAnimation(
    visible: Boolean,
    initialAlpha: Float = 0.0f,
    targetAlpha: Float = 1.0f,
    animationDuration: Int = 300,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            initialAlpha = initialAlpha,
            animationSpec = tween(durationMillis = animationDuration, delayMillis = 300)
        ),
        exit = fadeOut(
            targetAlpha = targetAlpha
        )
    ) {
        content()
    }
}