package ui.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mabia.composeapp.generated.resources.Res
import mabia.composeapp.generated.resources.gradient_07
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource
import ui.component.GlassyButton


@OptIn(ExperimentalResourceApi::class)
@Composable
fun Onboarding1(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        } else {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = imageResource(resource = Res.drawable.gradient_07),
                contentScale = ContentScale.Crop,
                contentDescription = "Welcome"
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 72.dp, top = 128.dp)
                .alpha(.95F),
            text = "Halo,",
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 52.sp,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = .25f),
                    blurRadius = 16f,
                    offset = Offset(2f, -2f)
                )
            ),
        )
        GlassyButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 200.dp),
            buttonText = {
                Text(
                    modifier = Modifier.defaultMinSize(minWidth = 128.dp),
                    text = "Mulai",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        ) {
            onClick()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Onboarding2(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        } else {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = imageResource(resource = Res.drawable.gradient_07),
                contentScale = ContentScale.Crop,
                contentDescription = "Welcome"
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 72.dp, top = 128.dp)
                .alpha(1f),
            text = "Mood kamu kalau\nberupa emoji\nseperti apa?",
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 24.sp,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = .25f),
                    blurRadius = 16f,
                    offset = Offset(2f, -2f)
                )
            ),
            lineHeight = TextUnit(30F, TextUnitType.Sp),
        )
        GlassyButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 200.dp),
            buttonText = { Text(text = "Pilih Emoji", style = MaterialTheme.typography.bodyLarge) }
        ) {
            onClick()
        }
    }
}