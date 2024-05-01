package ui.screen.history

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate

@Stable
data class DateMood(
    val moodRate: Int,
    val emojiUnicode: String,
    val date: LocalDate
)
