package ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.EmojiList
import getScreenSizeInfo

@Composable
fun ShareableView(
    moodRate: Int,
    emojiList: List<String>,
    formattedSelectedDate: String,
) {

    Surface {
        val emojiMoodBackground = MaterialTheme.colorScheme.surfaceContainerHighest
        val screenSize = getScreenSizeInfo()
        Box(modifier = Modifier.size(width = screenSize.wDP, height = screenSize.hDP)) {
            RandomEmojiCircularLayout2(
                background = MaterialTheme.colorScheme.surfaceContainerHighest,
                emojis = emojiList
            )

            Box(
                modifier = Modifier
                    .size(width = screenSize.wDP, height = screenSize.hDP)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = .75F)),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(emojiMoodBackground)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            MoodRateView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(emojiMoodBackground),
                                moodRate = moodRate,
                                loadingState = false,
                                staticMode = true
                            )
                        }
                        val moodLabel = EmojiList.moodPleasantness[moodRate].orEmpty()
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = moodLabel,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = formattedSelectedDate,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}