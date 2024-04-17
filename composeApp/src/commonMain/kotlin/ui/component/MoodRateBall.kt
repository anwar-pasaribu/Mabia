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
import config.PlatformType
import data.EmojiList
import getPlatform
import kotlinx.collections.immutable.toImmutableList
import mabia.composeapp.generated.resources.Res
import mabia.composeapp.generated.resources.bad
import mabia.composeapp.generated.resources.good
import mabia.composeapp.generated.resources.neutral
import mabia.composeapp.generated.resources.very_bad
import mabia.composeapp.generated.resources.very_good
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
        mutableStateOf(getBackgroundColorForMood(moodRate))
    }

    LaunchedEffect(moodRate, loadingState) {
        bgColor = getBackgroundColorForMood(moodRate)
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
        getBackgroundColorForMood(it)[0]
    }
    val updatedColorAnimVal2 by updatedTransition.animateColor(
        transitionSpec = { tween(durationMillis = 2000, easing = EaseOutCirc) }, label = "color2"
    ) {
        getBackgroundColorForMood(it)[1]
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
                    if (getPlatform().type == PlatformType.ANDROID) {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            MoodRateDisplay(moodRate)
                        }
                    } else {
                        val imgRes = when (moodRate) {
                            EmojiList.MOOD_1 -> Res.drawable.very_good
                            EmojiList.MOOD_2 -> Res.drawable.good
                            EmojiList.MOOD_3 -> Res.drawable.good
                            EmojiList.MOOD_4 -> Res.drawable.neutral
                            EmojiList.MOOD_5 -> Res.drawable.bad
                            EmojiList.MOOD_6 -> Res.drawable.bad
                            EmojiList.MOOD_7 -> Res.drawable.very_bad
                            else -> Res.drawable.neutral
                        }
                        ImageWrapper(
                            modifier = Modifier.align(Alignment.Center).size(128.dp),
                            resource = imgRes,
                            contentDescription = moodRate.toString()
                        )
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

private fun getBackgroundColorForMood(moodRating: Int): List<Color> {
    return when (moodRating) {
        EmojiList.MOOD_1 -> listOf(
            Color(0xFF50C878),
            Color(0xFF00755E)
        ) // Vibrant and modern color for very pleasant mood
        EmojiList.MOOD_2 -> listOf(Color(0xFF71C5E8), Color(0xFF0C81E6)) // Pleasant mood
        EmojiList.MOOD_3 -> listOf(Color(0xFFFFD700), Color(0xFFFFA500)) // Slightly pleasant mood
        EmojiList.MOOD_4 -> listOf(Color(0xFFFFFFFF), Color(0xFFD3D3D3)) // Neutral mood
        EmojiList.MOOD_5 -> listOf(Color(0xFFFF6347), Color(0xFFCD5C5C)) // Slightly unpleasant mood
        EmojiList.MOOD_6 -> listOf(Color(0xFF8B0000), Color(0xFFB22222)) // Unpleasant mood
        EmojiList.MOOD_7 -> listOf(Color(0xFF696969), Color(0xFF363636)) // Very unpleasant mood
        else -> listOf(
            Color(0xFFE0E0E0),
            Color(0xFFBEBEBE)
        ) // Default grayish gradient for unknown mood
    }.toImmutableList()
}