package ui.component

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun GlassyButton(
    modifier: Modifier = Modifier,
    buttonText: @Composable () -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animationTransition = updateTransition(isPressed, label = "BouncingClickableTransition")
    val scaleFactor by animationTransition.animateFloat(
        targetValueByState = { pressed -> if (pressed) .975f else 1f },
        label = "BouncingClickableScaleFactorTransition",
    )
    val opacity by animationTransition.animateFloat(
        targetValueByState = { pressed -> if (pressed) 0.95f else 1f },
        label = "BouncingClickableOpacityTransition"
    )
    val shadow by animationTransition.animateDp(
        targetValueByState = { pressed -> if (pressed) 2.dp else 16.dp },
        label = "BouncingClickableShadowTransition"
    )

    Button(
        modifier = modifier.then(
            Modifier.defaultMinSize(minWidth = 256.dp).graphicsLayer {
                this.scaleX = scaleFactor
                this.scaleY = scaleFactor
                this.alpha = opacity
                this.shape = RoundedCornerShape(percent = 50)
                this.shadowElevation = shadow.value
                this.spotShadowColor = DefaultShadowColor.copy(alpha = .85F)
            }
        ),
        enabled = enabled,
        onClick = { onClick() },
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
    ) {
        buttonText()
    }
}