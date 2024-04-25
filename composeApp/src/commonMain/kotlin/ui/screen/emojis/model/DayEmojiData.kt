package ui.screen.emojis.model

import kotlinx.datetime.LocalDate

data class DayEmojiData(
    val day: LocalDate,
    val emoji: String,
    val moodRateValue: Int
)
