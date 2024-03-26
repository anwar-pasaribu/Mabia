import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import di.appModule
import di.databaseModule
import di.platformModule
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import ui.screen.emojis.EmojiListScreen
import ui.screen.history.HistoryScreen
import ui.screen.onboarding.Onboarding1
import ui.screen.onboarding.Onboarding2
import ui.screen.reward.CongratulateScreen
import ui.theme.MyAppTheme
import ui.viewmodel.MainViewModel

@Composable
@Preview
fun App() {
    KoinApplication(
        application = { modules(platformModule() + appModule() + databaseModule()) }
    ) {
        PreComposeApp {
            MyAppTheme {

                val navigator = rememberNavigator()
                val viewModel = koinInject<MainViewModel>()

                val initialScreen = if (viewModel.onboardingFinished()) {
                    "/home"
                } else {
                    "/onboarding1"
                }

                NavHost(
                    navigator = navigator,
                    initialRoute = initialScreen,
                ) {
                    scene(
                        route = "/home",
                    ) {
                        EmojiListScreen(
                            onScreenStateChanged = {
                                navigator.navigate("/congratulation")
                            },
                            openHistoryScreen = {
                                navigator.navigate("/history")
                            }
                        )
                    }
                    scene(route = "/history") {
                        HistoryScreen() {
                            navigator.goBack()
                        }
                    }
                    scene(
                        route = "/congratulation",
                        navTransition = NavTransition(
                            createTransition = slideInVertically(initialOffsetY = { it }),
                            destroyTransition = fadeOut() + slideOutVertically(
                                animationSpec = tween(
                                    durationMillis = 300,
                                    delayMillis = 0,
                                    easing = EaseInOut
                                ),
                                targetOffsetY = { it }),
                            pauseTransition = scaleOut(targetScale = 0.9f),
                            resumeTransition = scaleIn(initialScale = 0.9f),
                            exitTargetContentZIndex = 1f,
                        ),
                    ) {
                        CongratulateScreen {
                            viewModel.saveFinishedOnboarding()
                            navigator.goBack()
                        }
                    }
                    scene(route = "/onboarding1") {
                        Onboarding1 {
                            navigator.navigate("/onboarding2")
                        }
                    }
                    scene(route = "/onboarding2") {
                        Onboarding2 {
                            navigator.navigate(
                                "/home",
                                NavOptions(
                                    // Launch the scene as single top
                                    launchSingleTop = true,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}