import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.EmojiList
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import kotlinx.coroutines.delay
import mabia.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.extension.bouncingClickable

@Composable
actual fun PlayHapticAndSound(trigger: Any) {
}

@Composable
actual fun MoodRateDisplay(moodRate: Int) {

    var playing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(moodRate) {
        delay(300)
        playing = true
    }

    Box(modifier = Modifier
        .bouncingClickable(enabled = !playing) {
            playing = !playing
        }) {
        ComposeLottieAnimation(moodRate = moodRate, playing) {
            playing = false
        }
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ComposeLottieAnimation(moodRate: Int, play: Boolean, onFinished: () -> Unit) {

    var lottieJsonString by remember {
        mutableStateOf("")
    }
    LaunchedEffect(moodRate) {
        val lottieJson = when (moodRate) {
            EmojiList.MOOD_1 -> "files/u1f60d_heart_eyes.json"
            EmojiList.MOOD_2 -> "files/u1f603_smile-with-big-eyes.json"
            EmojiList.MOOD_3 -> "files/u1f604_grin.json"
            EmojiList.MOOD_4 -> "files/u1f610_neutral_face.json"
            EmojiList.MOOD_5 -> "files/u1f61e_sad.json"
            EmojiList.MOOD_6 -> "files/u1f629_weary.json"
            EmojiList.MOOD_7 -> "files/u1f62b_distraught.json"
            else -> "files/u1fae5_dotted_line_face.json"
        }
        lottieJsonString = Res.readBytes(lottieJson).decodeToString()
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.JsonString(lottieJsonString)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        modifier = Modifier.size(128.dp),
        composition = composition,
        progress = { progress }
    )

    LaunchedEffect(progress) {
        if (progress >= .95F) {
            onFinished()
        }
    }
}