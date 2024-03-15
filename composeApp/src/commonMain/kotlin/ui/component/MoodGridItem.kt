package ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ui.extension.bouncingClickable

@Composable
fun MoodGridItem(
    content: @Composable () -> Unit
) {

    var isPressed by remember { mutableStateOf(false) }

    val animationTransition = updateTransition(isPressed, label = "ScalingBoxTransition")
    val roundedCornerAnimationVal by animationTransition.animateDp(
        targetValueByState = { pressed -> if (pressed) 16.dp else 28.dp },
        label = "RoundedCornerTransition",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
            )
        }
    )

    val sizeWidthAnimation by animationTransition.animateFloat(
        targetValueByState = { pressed ->
            if (pressed) {
                1.25f
            } else {
                1f
            }
        },
        label = "SizeWidthTransition",
        transitionSpec = {
            spring(dampingRatio = Spring.DampingRatioLowBouncy)
        }
    )

    val sizeHeightAnimation by animationTransition.animateFloat(
        targetValueByState = { pressed ->
            if (pressed) {
                1.25f
            } else {
                1f
            }
        },
        label = "SizeHeightTransition",
        transitionSpec = {
            spring(dampingRatio = Spring.DampingRatioLowBouncy)
        }
    )

    val offsetAnimationVal by animationTransition.animateDp(
        targetValueByState = { pressed ->
            if (pressed) {
                0.dp
            } else {
                0.dp
            }
        },
        label = "OffsetYTransition",
    )

    val shadowAnimationVal by animationTransition.animateFloat(
        targetValueByState = { pressed ->
            if (pressed) { 16F } else { 0F }
        },
        label = "ElevationShadowTransition",
    )

    val zIndexAnimVal by animationTransition.animateFloat(
        targetValueByState = { pressed ->
            if (pressed) { 2F } else { 1F }
        },
        label = "ElevationShadowTransition",
    )


    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = sizeWidthAnimation
                scaleY = sizeHeightAnimation
                shadowElevation = shadowAnimationVal
            }
            .offset(x = 0.dp, offsetAnimationVal)
            .bouncingClickable { isPressed = !isPressed }
            .clip(RoundedCornerShape(roundedCornerAnimationVal))
            .zIndex(zIndexAnimVal)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}