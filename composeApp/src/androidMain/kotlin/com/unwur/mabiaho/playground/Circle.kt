
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.MyAppTheme
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun EmojiCircle() {
    val emojis = listOf("😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣") // Add more emojis if needed
    val radius = 200.dp // Radius of the circle
    val center = Offset(300f, 300f) // Center of the circle

    val paint = android.graphics.Paint().apply {
        color = Color.Black.toArgb()
        textSize = 30f
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val angleStep = 2 * PI / emojis.size // Calculate angle step for distributing emojis evenly
        var currentAngle = PI / 2 // Start angle at 90 degrees (top of the circle)

        emojis.forEach { emoji ->
            // Calculate the position of the emoji on the circle
            val x = center.x + (radius.value * cos(currentAngle)).toFloat()
            val y = center.y + (radius.value * sin(currentAngle)).toFloat()

            // Draw the emoji at the calculated position
            drawIntoCanvas {
                it.nativeCanvas.drawText(emoji, x, y, paint)
            }

            // Increment the angle for the next emoji
            currentAngle += angleStep
        }

        // Draw the large circle
        drawCircle(color = Color.Blue, radius = radius.value, center = center)
    }
}

@Composable
fun EmojiCircularLayers() {
    val layers = 4 // Number of layers
    val emojisPerLayer = 8 // Number of emojis per layer
    val emojiSize = 30.dp // Size of emojis
    val center = Offset(300f, 300f) // Center of the circle
    val radiusStep = 50 // Step size for each layer
    val angleStep = 2 * PI / emojisPerLayer // Angle step for distributing emojis evenly

    Canvas(modifier = Modifier.fillMaxSize()) {
        for (layer in 0 until layers) {
            val currentRadius = (layer + 1) * radiusStep

            for (i in 0 until emojisPerLayer) {
                val currentAngle = i * angleStep

                // Calculate the position of the emoji on the circle
                val x = center.x + (currentRadius * cos(currentAngle)).toFloat()
                val y = center.y + (currentRadius * sin(currentAngle)).toFloat()

                // Draw the emoji at the calculated position
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        "😀", // Emojis can be replaced with your desired emoji
                        x - emojiSize.toPx() / 2,
                        y + emojiSize.toPx() / 2,
                        android.graphics.Paint()
                    )
                }
            }
        }

        // Draw the central circle
        drawCircle(color = Color.Blue, radius = 10.dp.toPx(), center = center)
    }
}

@Composable
fun RandomEmojiCircularLayout() {
    val layers = 3 // Number of layers
    val emojisPerLayer = 8 // Number of emojis per layer

    val minEmojiSize = 13.dp // Minimum emoji size
    val maxEmojiSize = 32.dp // Maximum emoji size
    val emojis = listOf("😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😎") // List of emojis

    val textMeasure = rememberTextMeasurer()
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            val canvasSize = size.width // Size of the canvas
            val center = Offset(size.width / 2, size.height / 2) // Center of the circle
            val maxRadius = size.width / 2 // Maximum radius

            repeat(layers) { layer ->
                val radius = maxRadius * (layer + 1) / layers // Adjust radius based on layer

                repeat(emojisPerLayer) {
                    val emoji = emojis.random() // Randomly select an emoji from the list
                    val emojiSize = Random.nextDouble(
                        minEmojiSize.value.toDouble(),
                        maxEmojiSize.value.toDouble()
                    ).dp // Random emoji size

                    val angle = Random.nextDouble(0.0, 2 * PI) // Random angle
                    val angleToCenter = kotlin.math.atan2(
                        center.y - (center.y + radius * sin(angle)).toFloat(),
                        center.x - (center.x + radius * cos(angle)).toFloat()
                    )

                    // Calculate the position of the emoji on the circle
                    val x = center.x + (radius * cos(angleToCenter)).toFloat()
                    val y = center.y + (radius * sin(angleToCenter)).toFloat()

                    // Draw the emoji at the calculated position
                    drawText(
                        textMeasurer = textMeasure,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontSize = emojiSize.toSp())) {
                                append(emoji)
                            }
                        },
                        topLeft = Offset(x - emojiSize.toPx() / 2, y + emojiSize.toPx() / 2)
                    )
                }
            }

            // Draw the central circle
            drawCircle(color = Color.Blue, radius = 10.dp.toPx(), center = center)
        }
    }
}

@Composable
fun RandomEmojiCircularLayout2() {
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



@Composable
fun EmojiCircularLayersScreen() {
    EmojiCircularLayers()
}

@Composable
fun EmojiCircleScreen() {
    EmojiCircle()
}

@Preview
@Composable
private fun CircleMojPrev() {
    MyAppTheme {
        Surface {
            Column {
                //EmojiCircleScreen()
//                EmojiCircularLayers()
//                RandomEmojiCircularLayout()
                RandomEmojiCircularLayout2() // Almost
            }
        }
    }
}