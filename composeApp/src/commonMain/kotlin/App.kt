import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.screen.emojis.EmojiListScreen
import ui.screen.onboarding.Onboarding1
import ui.screen.onboarding.Onboarding2
import ui.screen.reward.CongratulateScreen
import ui.theme.MyAppTheme

@Composable
@Preview
fun App() {
    PreComposeApp {
        MyAppTheme {
            val navigator = rememberNavigator()
            NavHost(
                navigator = navigator,
                navTransition = NavTransition(),
                initialRoute = "/onboarding1",
            ) {
                scene(
                    route = "/home",
                    navTransition = NavTransition(),
                ) {
                    EmojiListScreen {
                        navigator.navigate("/congratulation")
                    }
                }
                scene(
                    route = "/congratulation",
                    navTransition = NavTransition(
                        createTransition = slideInVertically(initialOffsetY = { it }),
                        destroyTransition = fadeOut() + slideOutVertically(
                            animationSpec = tween(durationMillis = 300, delayMillis = 0, easing = EaseInOut),
                            targetOffsetY = { it }),
                        pauseTransition = scaleOut(targetScale = 0.9f),
                        resumeTransition = scaleIn(initialScale = 0.9f),
                        exitTargetContentZIndex = 1f,
                    ),
                ) {
                    CongratulateScreen {
                        navigator.goBack()
                    }
                }
                scene(route = "/onboarding1") {
                    Onboarding1 {
                        navigator.navigate("/onboarding2")
                    }
                }
                scene(route = "/onboarding2", navTransition = NavTransition()) {
                    Onboarding2 {
                        navigator.navigate("/home")
                    }
                }
            }
        }
    }
}