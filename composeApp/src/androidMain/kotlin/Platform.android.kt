
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.util.UUID

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
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