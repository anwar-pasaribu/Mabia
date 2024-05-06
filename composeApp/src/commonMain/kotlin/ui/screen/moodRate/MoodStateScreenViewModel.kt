package ui.screen.moodRate

import data.EmojiList
import domain.usecase.GetEmojisByDateUseCase
import domain.usecase.GetMoodRateCalculationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.ISqlStorageRepository
import repo.KeyValueStorageRepository
import ui.screen.emojis.model.EmojiUiModel

class MoodStateScreenViewModel(
    private val kvsRepo: KeyValueStorageRepository,
    private val sqlStorageRepository: ISqlStorageRepository,
    private val getEmojisByDateUseCase: GetEmojisByDateUseCase,
    private val getMoodRateCalculationUseCase: GetMoodRateCalculationUseCase,
) : ViewModel() {

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())
    private val _moodRate = MutableStateFlow(EmojiList.MOOD_UNKNOWN)
    val moodRate: StateFlow<Int> = _moodRate.asStateFlow()

    private val emojiUnicodeList = hashMapOf<String, Int>()

    fun getEmojiByTimeStampRange(untilTimeStampMillis: Long) {
        val tz = TimeZone.currentSystemDefault()
        val selectedDateTime = Instant.fromEpochMilliseconds(
            untilTimeStampMillis
        ).toLocalDateTime(tz)

        viewModelScope.launch {
            _moodRate.value = EmojiList.MOOD_UNKNOWN
            emojiListStateFlow.emit(emptyList())
            emojiUnicodeList.clear()

            val emojiUiModelList = getEmojisByDateUseCase.invoke(selectedDateTime.date)
            emojiListStateFlow.emit(emojiUiModelList)
            _moodRate.value = getMoodRateCalculationUseCase.invoke(
                emojiUiModelList.map { it.emojiUnicode }
            )

        }
    }
}