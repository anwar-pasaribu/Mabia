package ui.screen.reward

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mabia.composeapp.generated.resources.Res
import mabia.composeapp.generated.resources.gradient_07
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource
import org.koin.compose.koinInject
import ui.component.GlassyButton
import ui.viewmodel.MainViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CongratulateScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val viewModel = koinInject<MainViewModel>()

    Box(modifier = Modifier.fillMaxSize()) {
        if (LocalInspectionMode.current) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray))
        } else {
            Image(
                modifier = Modifier.fillMaxSize().scale(1.5F, 1.5F),
                bitmap = imageResource(resource = Res.drawable.gradient_07),
                contentScale = ContentScale.Crop,
                contentDescription = "Welcome"
            )
        }
        Column(modifier = Modifier
            .align(Alignment.TopStart)
            .padding(start = 72.dp, top = 128.dp)
        ) {
            Text(
                modifier = Modifier.wrapContentSize(),
                text = "\uD83C\uDF89",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 72.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = .25f), blurRadius = 16f, offset = Offset(2f, -2f)
                    )
                ),
                lineHeight = TextUnit(30F, TextUnitType.Sp),
            )
            Text(
                modifier = Modifier
                    .padding(top = 72.dp),
                text = "Berhasil",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = .25f), blurRadius = 16f, offset = Offset(2f, -2f)
                    )
                ),
                lineHeight = TextUnit(30F, TextUnitType.Sp),
            )
            Text(
                modifier = Modifier
                    .padding(top = 14.dp),
                text = "Kamu bisa pilih\nemoji lagi kapan aja\nsesuai mood kamu ya",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 18.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = .25f), blurRadius = 16f, offset = Offset(2f, -2f)
                    )
                ),
                lineHeight = TextUnit(20F, TextUnitType.Sp),
            )
        }

        GlassyButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 200.dp),
            buttonText = {
                Text(text = "Kembali ke Home", style = MaterialTheme.typography.labelLarge)
            }
        ) {
            viewModel.saveFinishedOnboarding()
            onDismiss()
        }
    }
}