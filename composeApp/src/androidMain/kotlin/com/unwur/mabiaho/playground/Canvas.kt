package com.unwur.mabiaho.playground

import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.MyAppTheme
import kotlin.random.Random

@Preview(showBackground = true)
@Composable
private fun CanvasPrev() {
    MyAppTheme {
        CanvasBubble()
    }
}

data class RectData(
    var size: Size,
    var offset: Offset
)

private val RainbowColors = listOf(
    Color(0xff9c4f96),
    Color(0xffff6355),
    Color(0xfffba949),
    Color(0xfffae442),
    Color(0xff8bd448),
    Color(0xff2aa8f2)
)

@Composable
fun CanvasBubble() {
    val textMeasure = rememberTextMeasurer()

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var offsetX2 by remember { mutableStateOf(250f) }
    var offsetY2 by remember { mutableStateOf(300f) }
    val rectList = mutableListOf<RectData>()
    var rectA = RectData(Size(200f, 300f), Offset(offsetX, offsetY))
    var rectB = RectData(Size(500f, 600f), Offset(offsetX2, offsetY2))
    rectList.add(rectA)
    rectList.add(rectB)

    var selectedRect: RectData? by remember { mutableStateOf(null) }

    val text = buildAnnotatedString {
        append("s")
        withStyle(
            style = SpanStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic
            )
        ) {
            append("Hello,")
        }
        withStyle(
            style = SpanStyle(
                brush = Brush.horizontalGradient(colors = RainbowColors),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("\uD83D\uDE0D")
        }
    }

    Canvas(
        modifier = Modifier
            .border(width = 10.dp, color = Color(0x26000000))
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { onPressOffset ->
                        val x = onPressOffset.x
                        val y = onPressOffset.y

                        selectedRect = null
                        rectList.forEach {
                            val rect = RectF(
                                it.offset.x,
                                it.offset.y,
                                it.offset.x + it.size.width,
                                it.offset.y + it.size.height
                            )
                            if (rect.contains(x, y)) selectedRect = it
                        }
                    },
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    when (selectedRect) {
                        rectA -> {
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                            rectA.offset = Offset(offsetX, offsetY) //update the offset
                        }

                        rectB -> {
                            offsetX2 += dragAmount.x
                            offsetY2 += dragAmount.y
                            rectB.offset = Offset(offsetX2, offsetY2) //update the offset
                        }
                    }
                }
            }
    ) {
        drawText(
            textMeasurer = textMeasure,
            text = text,
            topLeft = Offset(10.dp.toPx(), 10.dp.toPx())
        )
        repeat(10) {
            drawCircle(
                alpha = 1F,
                color = Color.Black,
                radius = Random.nextLong(5L, 100L).toFloat(),
                center = Offset(
                    Random.nextLong(0L, size.width.toLong()).toFloat(),
                    Random.nextLong(0L, size.height.toLong()).toFloat()
                ),
            )
        }

        val canvasQuadrantSize = size / 2F
        drawRect(
            topLeft = Offset(0f, 0f),
            color = Color.Green,
            size = canvasQuadrantSize
        )
        rectList.forEach {
            drawRect(
                color = Color.Blue,
                topLeft = it.offset,
                size = it.size,
                style = Stroke(width = 8f)
            )
        }
    }
}