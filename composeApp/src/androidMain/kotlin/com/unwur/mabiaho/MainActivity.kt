package com.unwur.mabiaho

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.PathEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ui.screen.history.MoodBottomSheetContent
import ui.theme.MyAppTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = isSystemInDarkTheme()

            // Update the edge to edge configuration to match the theme
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }
            App(shouldDarkTheme = darkTheme)
        }
    }
}


/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)


@PreviewLightDark
@Composable
fun PreviewPlaceholder() {
    MyAppTheme {
        MoodBottomSheetContent(formattedSelectedDate = "Senin, 21 April 2024", moodRate = 2)
//        Column(modifier = Modifier.fillMaxWidth(), ) {
//            GlassyButton(buttonText = {
//                Text(text = "Preview here ya")
//            }, onClick = {})
//
//            PathEasing()
//        }
    }
}

@OptIn(ExperimentalTransitionApi::class)
@Composable
fun TouchPointer() {
    // Creates an `Animatable` to animate Offset and `remember` it.
    val animatedOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xffb99aff))
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        val offset = this@pointerInput.awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        // Launch a new coroutine for animation so the touch detection thread is not
                        // blocked.
                        launch {
                            // Animates to the pressed position, with the given animation spec.
                            animatedOffset.animateTo(
                                offset,
                                animationSpec = spring(stiffness = Spring.StiffnessLow)
                            )
                        }
                    }
                }
            }
    ) {
        Text("Tap anywhere", Modifier.align(Alignment.Center))
        Box(
            Modifier
                .offset {
                    // Use the animated offset as the offset of the Box.
                    IntOffset(
                        animatedOffset.value.x.roundToInt(),
                        animatedOffset.value.y.roundToInt()
                    )
                }
                .size(40.dp)
                .background(Color(0xff3c1361), CircleShape)
        )
    }
}

@Composable
fun PathEasing() {

    // Creates a custom PathEasing curve and applies it to an animation
    var toggled by remember {
        mutableStateOf(true)
    }
    val pathForAnimation = remember {
        Path().apply {
            moveTo(0f, 0f)
            cubicTo(0.05f, 0f, 0.133333f, 0.06f, 0.166666f, 0.4f)
            cubicTo(0.208333f, 0.82f, 0.25f, 1f, 1f, 1f)
        }
    }
    val offset by animateIntOffsetAsState(
        targetValue =
        if (toggled) IntOffset.Zero else IntOffset(300, 300),
        label = "offset",
        animationSpec = tween(durationMillis = 1000, easing = PathEasing(pathForAnimation))
    )
    Box(modifier = Modifier
        .fillMaxSize()
        .clickable {
            toggled = !toggled
        }) {
        Box(modifier = Modifier
            .offset {
                offset
            }
            .size(100.dp)
            .background(Color.Blue))
    }

}
