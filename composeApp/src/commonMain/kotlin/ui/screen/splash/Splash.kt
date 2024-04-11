package ui.screen.splash

import ScreenRoute
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

            ImageWrapper(
                modifier = Modifier.size(288.dp),
                resource = Res.drawable.ic_logo_mabia_normal_eye,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentScale = ContentScale.Fit,
                contentDescription = "App Logo"
            )
        }
    }
}