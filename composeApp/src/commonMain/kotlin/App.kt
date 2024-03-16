import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.extension.FadeAnimation
import ui.screen.emojis.EmojiListScreen
import ui.screen.onboarding.Onboarding1
import ui.screen.onboarding.Onboarding2
import ui.screen.reward.CongratulateScreen
import ui.theme.MyAppTheme

@Composable
@Preview
fun App() {
    MyAppTheme {
        val contentState = remember { mutableStateOf(0) }
        AnimatedContent(
            targetState = contentState,
            transitionSpec = {
                fadeIn().plus(slideInVertically(initialOffsetY = { it * 1 /* BOTTOM to UP*/})).togetherWith(fadeOut())
            },
        ) {
            when(it.value) {
                0 -> {
                    Onboarding1(
                        onClick = {
                            contentState.value = 1
                        }
                    )
                }
                1 -> {
                    FadeAnimation(
                        visible = true
                    ) {
                        Onboarding2(
                            onClick = {
                                contentState.value = 2
                            }
                        )
                    }
                }
                2 -> {
                    EmojiListScreen {
                        contentState.value = 3
                    }
                }
                3 -> {
                    CongratulateScreen {
                        contentState.value = 2
                    }
                }
            }
        }
    }
}