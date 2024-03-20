package ui.extension

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

fun Modifier.delayedAlpha() = composed {

    var showLastSelected by remember { mutableStateOf(true) }
    LaunchedEffect(showLastSelected) {
        delay(1000L)
        showLastSelected = false
    }

    val animationTransition = updateTransition(showLastSelected, label = "DelayedFadingOut")
    val animVal by animationTransition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        },
        targetValueByState = { visible ->
            if (visible) {
                1F
            } else {
                0F
            }
        },
        label = "AlphaTransition",
    )

    this
        .graphicsLayer {
            this.alpha = animVal
            this.scaleX = animVal
            this.scaleY = animVal
        }
}