
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import config.PlatformType
import data.EmojiList
import kotlinx.coroutines.delay
import mabia.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.extension.bouncingClickable
import java.util.UUID

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    override val type: PlatformType
        get() = PlatformType.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}

actual fun getUUIDString(): String {
    return UUID.randomUUID().toString()
}

@Composable
actual fun getScreenSizeInfo(): ScreenSizeInfo {
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val hDp = config.screenHeightDp.dp
    val wDp = config.screenWidthDp.dp

    return remember(density, config) {
        ScreenSizeInfo(
            hPX = with(density) { hDp.roundToPx() },
            wPX = with(density) { wDp.roundToPx() },
            hDP = hDp,
            wDP = wDp
        )
    }
}

@Composable
actual fun PlayHapticAndSound(trigger: Any) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val vibrator = remember {
        ContextCompat.getSystemService(context, android.os.Vibrator::class.java)
    }
    val audioManager = remember {
        ContextCompat.getSystemService(context, android.media.AudioManager::class.java)
    }

    LaunchedEffect(trigger) {
        audioManager?.playSoundEffect(android.media.AudioManager.FX_KEYPRESS_STANDARD, 1f)
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        vibrator?.vibrate(
            android.os.VibrationEffect.createOneShot(
                50,
                android.os.VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }
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
    val progress by animateLottieCompositionAsState(composition = composition, isPlaying = play)
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