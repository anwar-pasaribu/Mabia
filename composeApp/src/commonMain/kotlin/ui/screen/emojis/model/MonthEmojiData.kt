package ui.screen.emojis.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate

data class MonthEmojiData(
    val month: LocalDate,
    val dailyEmojis: ImmutableList<DayEmojiData>
)
