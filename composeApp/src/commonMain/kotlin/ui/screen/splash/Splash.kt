package ui.screen.splash

import ScreenRoute
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import mabia.composeapp.generated.resources.Res
import mabia.composeapp.generated.resources.ic_logo_mabia_normal_eye
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.koinInject
import ui.component.ImageWrapper
import ui.viewmodel.MainViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Splash(launchScreen: (route: String) -> Unit) {

    val viewModel = koinInject<MainViewModel>()

    LaunchedEffect(true) {
        val initialScreen = if (viewModel.onboardingFinished()) {
            ScreenRoute.HOME
        } else {
            ScreenRoute.ONBOARDING1
        }

        delay(350)
        launchScreen(initialScreen)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        } else {
//            Image(
//                modifier = Modifier.fillMaxSize(),
//                bitmap = imageResource(resource = Res.drawable.gradient_07),
//                contentScale = ContentScale.Crop,
//                contentDescription = "Splash"
//            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )

            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(150)
                visible = true
            }
            val floatAnimateState by animateFloatAsState(
                targetValue = if (visible) 1.15f else 1F,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            ImageWrapper(
                modifier = Modifier.size(288.dp)
                    .scale(floatAnimateState),
                resource = Res.drawable.ic_logo_mabia_normal_eye,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentScale = ContentScale.Fit,
                contentDescription = "App Logo"
            )
        }
    }
}