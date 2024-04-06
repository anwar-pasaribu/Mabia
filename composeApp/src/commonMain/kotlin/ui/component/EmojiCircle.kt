package ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun RandomEmojiCircularLayout() {
    val layers = 8 // Number of layers
    val emojisPerLayer = 7 // Number of emojis per layer
    val minEmojiSize = 32.sp // Minimum emoji size
    val maxEmojiSize = 86.sp // Maximum emoji size
    val emojis = listOf("😀", "😃", "😄", "😁", "😆", "😅", "😂", "😊", "😎", "🤩") // List of emojis

    // Draw the emoji at the calculated position
    val textMeasure = rememberTextMeasurer()

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize().background(Color.Black).scale(1.7F)) {

            val canvasW = size.width
            val canvasH = size.height

            val center = Offset(canvasW / 2, canvasH / 2) // Center of the circle
            val maxRadius = canvasW.coerceAtMost(canvasH) / 2 // Maximum radius

            repeat(layers) { layer ->
                val radius = maxRadius * (layer + 1) / layers // Adjust radius based on layer
                val emojisOnLayer = (emojisPerLayer * (1.5 + layer * 0.3)).toInt() // Increase emojis per layer as we move towards the outer layers

                repeat(emojisOnLayer) { index ->
                    val emoji = emojis.random() // Random pick the emoji
                    // Calculate emoji size based on radius
                    val emojiSize = minEmojiSize.value + (maxEmojiSize.value - minEmojiSize.value) * (radius / maxRadius)
                    val angle = 2 * PI * index / emojisOnLayer // Angle around the center for this emoji

                    // Calculate the position of the emoji on the circle
                    val x = center.x + (radius * cos(angle)).toFloat()
                    val y = center.y + (radius * sin(angle)).toFloat()

                    // Calculate the angle of the emoji towards the center
                    val angleToCenter = atan2(center.y - y, center.x - x)

                    // Draw the rotated emoji
                    rotate(
                        degrees = angleToCenter * (180 / PI).toFloat() + 90,
                        pivot = Offset(x, y)
                    ) { // Rotate the canvas to make the emoji face towards the center
                        drawText(
                            textMeasurer = textMeasure,
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontSize = emojiSize.toSp())) {
                                    append(emoji)
                                }
                            },
                            topLeft = Offset(x - emojiSize / 2, y + emojiSize / 2)
                        )
                    }
                }
            }

            // Draw the central circle
            drawCircle(color = Color.Magenta, radius = 30.dp.toPx(), center = center)
        }
    }
}