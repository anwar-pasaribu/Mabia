package ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@Composable
fun EmojiFlyingUpCanvas(emojiFlow: Flow<Pair<String, Offset>>) {

    var emojis by remember { mutableStateOf(emptyList<AnimatedEmojis>()) }

    val textMeasure = rememberTextMeasurer()
    var emojiUnicode by remember { mutableStateOf("") }
    var emojiOffsetX by remember { mutableStateOf(0F) }
    var emojiOffsetY by remember { mutableStateOf(0F) }
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val blurAnimatable = remember { Animatable(8.dp.value) }


    LaunchedEffect(emojiFlow) {
        emojiFlow.collect { newEmojiFlow ->
            emojiUnicode = newEmojiFlow.first
            emojiOffsetX = newEmojiFlow.second.x
            emojiOffsetY = newEmojiFlow.second.y
        }
    }

    LaunchedEffect(emojiUnicode, emojiOffsetX, emojiOffsetY) {
        offsetX.snapTo(
            emojiOffsetX
        )
        offsetY.snapTo(
            emojiOffsetY
        )
        launch {
            offsetY.animateTo(
                targetValue = -320.dp.value,
                animationSpec = tween(
                    durationMillis = 600,
                    delayMillis = 0
                )
            )
            blurAnimatable.animateTo(targetValue = 32.dp.value, animationSpec = tween(durationMillis = 2000))
        }
        emojis = listOf(AnimatedEmojis(emojiUnicode, emojiOffsetX, emojiOffsetY, offsetY)) + emojis
    }

    Canvas(
        modifier = Modifier.fillMaxSize().blur(blurAnimatable.value.dp)
    ) {
        drawText(
            textMeasurer = textMeasure,
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = 92.8.sp)) {
                    append(emojiUnicode)
                }
            },
            topLeft = Offset(offsetX.value - 28.dp.value, offsetY.value - 28.dp.value)
        )
    }
}

data class AnimatedEmojis(
    val unicode: String,
    val x: Float,
    val y: Float,
    var animatedY: Animatable<Float, AnimationVector1D>
)

@Composable
fun CanvasWithAnimatedCircle() {
    var circles by remember { mutableStateOf(emptyList<AnimatedCircle>()) }
    val coroutineScope = rememberCoroutineScope()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newCircle = AnimatedCircle(offset.x, offset.y, 50.dp.toPx())
                    circles = listOf(newCircle) + circles
                    animateCircle(coroutineScope, newCircle)
                }
            }
    ) {
        circles.forEach { circle ->
            drawCircle(Color.Blue, radius = circle.radius, center = Offset(circle.x, circle.y))
        }
    }
}

data class AnimatedCircle(
    var x: Float,
    var y: Float,
    val radius: Float
)

fun animateCircle(coroutineScope: CoroutineScope, circle: AnimatedCircle) {
    val animY = Animatable(circle.y)
    animY.updateBounds(0f, 2000f) // Adjust bounds as per requirement

    coroutineScope.launch {
        animY.animateTo(0f, animationSpec = tween(durationMillis = 1000)) {
            circle.y = value
        }
    }
}