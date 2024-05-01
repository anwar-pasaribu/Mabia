package ui.screen.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import network.chaintech.composeMultiplatformScreenCapture.ScreenCaptureComposable
import network.chaintech.composeMultiplatformScreenCapture.rememberScreenCaptureController
import ui.component.ShareableView
import ui.theme.MyAppTheme

@Composable
fun ShareMoodRateDialog(
    onDismiss: () -> Unit,
    moodRate: Int,
    emojis: List<String>,
    formattedDate: String
) {

    val captureController = rememberScreenCaptureController()

    val dialogProperties = DialogProperties(usePlatformDefaultWidth = false)

    val emojiList = mutableListOf<String>()
    if (emojis.isEmpty()) {
        emojiList.addAll(listOf(""))
    } else {
        emojiList.addAll(emojis)
    }

    LaunchedEffect(Unit) {
        delay(300)
        captureController.capture()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = dialogProperties
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

            ScreenCaptureComposable(
                modifier = Modifier.fillMaxSize(),
                screenCaptureController = captureController,
                shareImage = true,
                onCaptured = { img, throwable ->
                }
            ) {
                MyAppTheme {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            ShareableView(
                                moodRate = moodRate,
                                formattedSelectedDate = formattedDate,
                                emojiList = emojiList
                            )
                        }
                    }
                }
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(24.dp)
                    .clip(CircleShape)
                    .size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black
                ),
                onClick = {
                    onDismiss()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp)
                    .clip(CircleShape)
                    .size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black
                ),
                onClick = {
                    captureController.capture()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }
        }
    }
}