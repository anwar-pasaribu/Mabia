package ui.component.bubble


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@Composable
fun IconRounded(
    modifier: Modifier = Modifier,
    emojiUnicode: String
) {
    Box(
        modifier = Modifier.then(modifier),
        contentAlignment = Alignment.Center
    ) {
//        Image(
//            modifier = Modifier.fillMaxSize(),
//            painter = painterResource(id = res),
//            contentDescription = ""
//        )
        Text(
            modifier = Modifier.fillMaxSize(),
            text = emojiUnicode,
            fontSize = TextUnit(64F, TextUnitType.Sp)
        )
    }
}

@Composable
fun CrossLine(
    color: Color = Color.White
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize(),
        onDraw = {
            drawLine(
                start = Offset(this.size.width / 2, 0f),
                end = Offset(this.size.width / 2, this.size.height),
                color = color,
                strokeWidth = 4f
            )

            drawLine(
                start = Offset(0f, this.size.height / 2),
                end = Offset(this.size.width, this.size.height / 2),
                color = color,
                strokeWidth = 4f
            )
        }
    )
}