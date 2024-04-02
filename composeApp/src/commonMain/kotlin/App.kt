import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import ui.screen.emojis.EmojiListScreen
import ui.screen.history.HistoryScreen
import ui.screen.onboarding.Onboarding1
import ui.screen.onboarding.Onboarding2
import ui.screen.reward.CongratulateScreen
import ui.screen.splash.Splash
import ui.theme.MyAppTheme

@Composable
@Preview
fun App(shouldDarkTheme: Boolean = isSystemInDarkTheme()) {
//    KoinApplication(
//        application = {
//            modules(platformModule() + appModule() + databaseModule())
//        }
//    ) {
    PreComposeApp {
        KoinContext {
            MyAppTheme {
                val navigator = rememberNavigator()
                NavHost(
                    navigator = navigator,
                    initialRoute = ScreenRoute.SPLASH,
                    navTransition = NavTransition(
                        createTransition = fadeIn(),
                        destroyTransition = fadeOut(),
                        pauseTransition = fadeOut(),
                        resumeTransition = fadeIn(),
                        exitTargetContentZIndex = 1f,
                    )
                ) {
                    scene(
                        ScreenRoute.SPLASH, navTransition = NavTransition(
                            createTransition = EnterTransition.None,
                            destroyTransition = ExitTransition.None,
                            pauseTransition = ExitTransition.None,
                            resumeTransition = EnterTransition.None,
                        )
                    ) {
                        Splash { screenRoute ->
                            navigator.navigate(
                                screenRoute,
                                NavOptions(
                                    // Launch the scene as single top
                                    launchSingleTop = true,
                                    popUpTo = PopUpTo.First()
                                )
                            )
                        }
                    }
                    scene(
                        route = ScreenRoute.HOME,
                    ) {
                        EmojiListScreen(
                            onScreenStateChanged = {
                                navigator.navigate(ScreenRoute.CONGRATULATE)
                            },
                            openHistoryScreen = {
                                navigator.navigate(ScreenRoute.HISTORY)
                            }
                        )
                    }
                    scene(route = ScreenRoute.HISTORY) {
                        HistoryScreen() {
                            navigator.goBack()
                        }
                    }
                    scene(
                        route = ScreenRoute.CONGRATULATE,
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
                            navigator.goBack()
                        }
                    }
                    scene(route = ScreenRoute.ONBOARDING1) {
                        Onboarding1 {
                            navigator.navigate(
                                ScreenRoute.ONBOARDING2,
                                NavOptions(
                                    launchSingleTop = true,
                                    popUpTo = PopUpTo.First()
                                )
                            )
                        }
                    }
                    scene(route = ScreenRoute.ONBOARDING2) {
                        Onboarding2 {
                            navigator.navigate(
                                ScreenRoute.HOME,
                                NavOptions(
                                    launchSingleTop = true,
                                    popUpTo = PopUpTo.First()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
//    }
}

object ScreenRoute {
    const val SPLASH = "/splash"
    const val HOME = "/home"
    const val HISTORY = "/history"
    const val ONBOARDING1 = "/onboarding1"
    const val ONBOARDING2 = "/onboarding2"
    const val CONGRATULATE = "/congratulation"
}