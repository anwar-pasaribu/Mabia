package ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ui.extension.bouncingClickable

@Composable
fun MoodGridItem(
    modifier: Modifier = Modifier,
    content: String,
    viewOnlyMode: Boolean = false,
    onSelect: (String, Offset) -> Unit = { _, _ -> }
) {

    val emojiUnicode = remember { content }
    var isSelected by remember { mutableStateOf(false) }
    var emojiCenterOffset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(isSelected) {
        delay(300)
        isSelected = false
    }

    val animationTransition = updateTransition(isSelected, label = "ScalingBoxTransition")
    val sizeAnimation by animationTransition.animateSize(
        targetValueByState = { pressed ->
            if (pressed) {
                Size(1.45F, 1.45F)
            } else {
                Size(1F, 1F)
            }
        },
        label = "SizeTransition",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        }
    )

    val alphaAnimVal by animationTransition.animateFloat(
        targetValueByState = { pressed ->
            if (pressed) {
                0F
            } else {
                1F
            }
        },
        label = "AlphaTransition",
    )

    val shape = remember { RoundedCornerShape(24.dp) }
    val background = if (!viewOnlyMode) {
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(
            alpha = alphaAnimVal
        )
    } else {
        Color.Transparent
    }

    Box(
        modifier = modifier.then(
            Modifier.fillMaxSize()
                .scale(sizeAnimation.width, sizeAnimation.height)
                .bouncingClickable(enabled = !isSelected && !viewOnlyMode) {
                    isSelected = !isSelected
                    onSelect(content, emojiCenterOffset)
                }
                .clip(shape)
                .aspectRatio(1F)
                .background(
                    background
                )
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.onGloballyPositioned {
                    emojiCenterOffset = it.boundsInRoot().topLeft
                }.graphicsLayer {
                    alpha = alphaAnimVal
                },
                text = emojiUnicode,
                fontSize = TextUnit(64F, TextUnitType.Sp)
            )
        }
    }
}