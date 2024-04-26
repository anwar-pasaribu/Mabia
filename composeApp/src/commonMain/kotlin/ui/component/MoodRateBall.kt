package ui.component

import MoodRateDisplay
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import data.EmojiList
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MoodRateView(
    modifier: Modifier = Modifier,
    moodRate: Int = -1,
    loadingState: Boolean = true
) {

    val isLoading = remember { mutableStateOf(loadingState) }

    var bgColor by remember {
        mutableStateOf(EmojiList.getBackgroundColorForMood(moodRate))
    }

    LaunchedEffect(moodRate, loadingState) {
        bgColor = EmojiList.getBackgroundColorForMood(moodRate)
        isLoading.value = loadingState
    }
    val loadingTransition = updateTransition(isLoading, label = "LoadingTransition")
    val loadingDpVal by loadingTransition.animateDp(
        transitionSpec = { tween(durationMillis = 600, easing = FastOutSlowInEasing) },
        label = "blurVal"
    ) {
        if (it.value) {
            32.dp
        } else {
            0.dp
        }
    }

    val updatedTransition = updateTransition(moodRate, label = "UpdateRateTransition")
    val updatedColorAnimVal1 by updatedTransition.animateColor(
        transitionSpec = { tween(durationMillis = 2000, easing = EaseOutCirc) }, label = "color1"
    ) {
        EmojiList.getBackgroundColorForMood(it)[0]
    }
    val updatedColorAnimVal2 by updatedTransition.animateColor(
        transitionSpec = { tween(durationMillis = 2000, easing = EaseOutCirc) }, label = "color2"
    ) {
        EmojiList.getBackgroundColorForMood(it)[1]
    }

    val colorAnimation = rememberInfiniteTransition(label = "PulsatingMoodRate")
    val color by colorAnimation.animateColor(
        initialValue = bgColor.first(),
        targetValue = bgColor.last(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = "colorAnimValue"
    )

    val size by colorAnimation.animateFloat(
        initialValue = .65f,
        targetValue = .75f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = "sizeAnimValue"
    )

    val rotate by colorAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "rotateAnimValue"
    )
    Surface {
        Box(
            modifier = modifier.then(Modifier.blur(loadingDpVal)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(18.dp)
                        .padding(32.dp)
                        .scale(size, size)
                        .alpha(.25F)
                        .aspectRatio(1F)
                        .clip(CircleShape)
                        .background(color),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(36.dp)
                        .padding(48.dp)
                        .alpha(.45F)
                        .aspectRatio(1F)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    updatedColorAnimVal1,
                                    updatedColorAnimVal2
                                )
                            )
                        ),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize(.65F)
                        .aspectRatio(1F)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    updatedColorAnimVal1,
                                    updatedColorAnimVal2
                                )
                            )
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.Transparent
                            ),
                            shape = CircleShape
                        )
                ) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        MoodRateDisplay(moodRate)
                    }
                }
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotate
                        }
                        .fillMaxSize(.649F)
                        .aspectRatio(1F)
                        .clip(CircleShape)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    listOf(
                                        updatedColorAnimVal1,
                                        updatedColorAnimVal2
                                    )
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}
