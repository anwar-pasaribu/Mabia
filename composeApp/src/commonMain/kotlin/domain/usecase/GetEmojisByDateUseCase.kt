package domain.usecase

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import repo.ISqlStorageRepository
import ui.screen.emojis.model.EmojiUiModel

class GetEmojisByDateUseCase(
    private val sqlStorageRepository: ISqlStorageRepository,
) {

    suspend operator fun invoke(selectedLocalDate: LocalDate): List<EmojiUiModel> {
        val tz = TimeZone.currentSystemDefault()
        val endOfTimeStampOfSelectedDate = LocalTime(hour = 23, minute = 59, second = 59)
        val dateTime = LocalDateTime(
            date = selectedLocalDate,
            time = endOfTimeStampOfSelectedDate
        )
        val untilTimeStampMillis = dateTime.toInstant(tz).toEpochMilliseconds()

        val selectedDateTime = Instant.fromEpochMilliseconds(
            untilTimeStampMillis
        ).toLocalDateTime(tz)
        val firstSecondOfTheDay = LocalTime(hour = 0, minute = 0, second = 1)
        val dateTimeTodayMorning =
            LocalDateTime(date = selectedDateTime.date, time = firstSecondOfTheDay)
        val startTimeStampMillis = dateTimeTodayMorning.toInstant(tz).toEpochMilliseconds()

        return sqlStorageRepository.getEmojiByTimestampRange(
            startTimeStampMillis, untilTimeStampMillis
        ).map { emoji ->
            EmojiUiModel(id = emoji.id.toInt(), emojiUnicode = emoji.emojiUnicode)
        }

    }
}