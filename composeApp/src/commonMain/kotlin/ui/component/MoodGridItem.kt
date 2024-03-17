package ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ui.extension.bouncingClickable

@Composable
fun MoodGridItem(
    modifier: Modifier = Modifier,
    content: String,
    selected: Boolean = false,
    onSelect: (String) -> Unit
) {

    var isSelected by remember { mutableStateOf(selected) }

    val animationTransition = updateTransition(isSelected, label = "ScalingBoxTransition")
    val roundedCornerAnimationVal by animationTransition.animateDp(
        targetValueByState = { pressed -> if (pressed) 32.dp else 24.dp },
        label = "RoundedCornerTransition",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
            )
        }
    )

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

    Box(
        modifier = modifier.then(
            Modifier.fillMaxSize()
                .scale(sizeAnimation.width, sizeAnimation.height)
                .bouncingClickable {
                    isSelected = !isSelected
                    onSelect(content)
                }
                .clip(RoundedCornerShape(roundedCornerAnimationVal))
                .aspectRatio(1F)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHighest.copy(
                        alpha = alphaAnimVal
                    )
                )
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = content,
                fontSize = TextUnit(64F, TextUnitType.Sp)
            )
        }
    }
}