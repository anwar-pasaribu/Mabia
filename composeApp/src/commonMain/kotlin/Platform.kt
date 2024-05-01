import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp
import config.PlatformType

interface Platform {
    val name: String
    val type: PlatformType
}

expect fun getPlatform(): Platform

/**
 * Creates [ImageBitmap] from this [ByteArray]
 */
expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect fun getUUIDString(): String

/** Getting screen size info for UI-related calculations */
data class ScreenSizeInfo(val hPX: Int, val wPX: Int, val hDP: Dp, val wDP: Dp)
@Composable
expect fun getScreenSizeInfo(): ScreenSizeInfo

@Composable
expect fun PlayHapticAndSound(trigger: Any)

@Composable
expect fun MoodRateDisplay(moodRate: Int)
