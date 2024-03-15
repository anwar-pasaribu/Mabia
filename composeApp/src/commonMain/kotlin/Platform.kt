import androidx.compose.ui.graphics.ImageBitmap

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

/**
 * Creates [ImageBitmap] from this [ByteArray]
 */
expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect fun getUUIDString(): String