package ui.screen.splash

import ScreenRoute
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import mabia.composeapp.generated.resources.Res
import mabia.composeapp.generated.resources.logo_01
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource
import org.koin.compose.koinInject
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
                targetValue = if (visible) 1f else .85F,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            Image(
                modifier = Modifier.size(128.dp)
                    .scale(floatAnimateState)
                    .alpha(floatAnimateState)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { visible = !visible },
                bitmap = imageResource(resource = Res.drawable.logo_01),
                contentScale = ContentScale.Crop,
                contentDescription = "App Logo"
            )
        }
    }
}