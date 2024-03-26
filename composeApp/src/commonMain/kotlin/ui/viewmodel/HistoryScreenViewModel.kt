package ui.viewmodel

import androidx.compose.ui.util.fastMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.ISqlStorageRepository
import ui.screen.emojis.model.EmojiUiModel

class HistoryScreenViewModel(
    private val sqlStorageRepository: ISqlStorageRepository,
): ViewModel() {

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())

    init {
        viewModelScope.launch {
            sqlStorageRepository.getAllEmoji().collect { emojiList ->
                emojiListStateFlow.emit(emojiList.fastMap { emoji ->
                    EmojiUiModel(id = emoji.id.toInt(), emojiUnicode = emoji.emojiUnicode)
                })
            }
        }
    }

    fun getEmojiByTimeStampRange(untilTimeStampMillis: Long) {
        val nowLocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val firstSecondOfTheDay = LocalTime(hour = 0, minute = 0, second = 1)
        val dateTimeTodayMorning = LocalDateTime(date = nowLocalDate, time = firstSecondOfTheDay)
        val startTimeStampMillis = dateTimeTodayMorning.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

        viewModelScope.launch {
            sqlStorageRepository
                .getEmojiByTimestampRange(startTimeStampMillis, untilTimeStampMillis).collect { emojiList ->
                emojiListStateFlow.emit(emojiList.fastMap { emoji ->
                    EmojiUiModel(id = emoji.id.toInt(), emojiUnicode = emoji.emojiUnicode)
                })
            }
        }
    }
}