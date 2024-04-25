package ui.viewmodel

import androidx.compose.ui.util.fastMap
import data.EmojiList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.ISqlStorageRepository
import ui.screen.emojis.model.EmojiUiModel
import kotlin.math.roundToInt

class MoodStateScreenViewModel(
    private val sqlStorageRepository: ISqlStorageRepository,
) : ViewModel() {

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())
    val calenderListStateFlow = MutableStateFlow(emptyList<LocalDate>())
    private val _moodRate = MutableStateFlow(EmojiList.MOOD_UNKNOWN)
    val moodRate: StateFlow<Int> = _moodRate.asStateFlow()

    private val emojiUnicodeList = hashMapOf<String, Int>()

    fun getEmojiByTimeStampRange(untilTimeStampMillis: Long) {
        val tz = TimeZone.currentSystemDefault()
        val selectedDateTime = Instant.fromEpochMilliseconds(
            untilTimeStampMillis
        ).toLocalDateTime(tz)
        val firstSecondOfTheDay = LocalTime(hour = 0, minute = 0, second = 1)
        val dateTimeTodayMorning = LocalDateTime(date = selectedDateTime.date, time = firstSecondOfTheDay)
        val startTimeStampMillis = dateTimeTodayMorning.toInstant(tz).toEpochMilliseconds()

        viewModelScope.launch {
            _moodRate.value = EmojiList.MOOD_UNKNOWN
            emojiListStateFlow.emit(emptyList())
            emojiUnicodeList.clear()
            sqlStorageRepository
                .getEmojiByTimestampRangeObservable(startTimeStampMillis, untilTimeStampMillis)
                .collect { emojiList ->
                    emojiListStateFlow.emit(emojiList.fastMap { emoji ->
                        if (emojiUnicodeList.containsKey(emoji.emojiUnicode)) {
                            emojiUnicodeList[emoji.emojiUnicode] =
                                emojiUnicodeList[emoji.emojiUnicode]!! + 1
                        } else {
                            emojiUnicodeList[emoji.emojiUnicode] = 1
                        }
                        EmojiUiModel(id = emoji.id.toInt(), emojiUnicode = emoji.emojiUnicode)
                    })

                    calculatePercentage(emojiUnicodeList)
                }
        }
    }

    private fun calculatePercentage(emojiUnicodeList: HashMap<String, Int>) {
        if (emojiUnicodeList.isEmpty()) {
            return
        }

        val totalEmojis = emojiUnicodeList.values.sum()

        val percentages = mutableMapOf<String, Double>()

        for ((emoji, count) in emojiUnicodeList) {
            val percentage = (count.toDouble() / totalEmojis) * 100
            percentages[emoji] = percentage
        }

        val csv = StringBuilder()
        csv.append("Emoji\tPercentage\n")
        for ((emoji, percentage) in percentages) {
            csv.append("${emoji.trim()},${percentage}\n")
        }
        println(csv)
        val jsonString = buildString {
            append("{")
            percentages.onEachIndexed { index, (key, value) ->
                append("\"$key\": $value")
                if (index < percentages.size - 1) {
                    append(", ")
                }
            }
            append("}")
        }

        println(jsonString)

        _moodRate.value = calculateMoodRating(percentages)
    }

    /**
     * In Russell's circumplex model, affective states are represented in a two-dimensional space: valence (pleasure) and arousal (activation). Valence ranges from unpleasant to pleasant, while arousal ranges from low to high activation. We can use this model to assign mood ratings based on the valence of the emojis.
     *
     * Here's an approach to calculating the mood rating using Russell's circumplex model:
     */
    private fun calculateMoodRating(emojiMap: Map<String, Double>): Int {

        var totalWeightedFrequency = 0.0
        var totalFrequency = 0.0

        emojiMap.forEach { (emoji, frequency) ->
            val weight = EmojiList.emojiWeights[emoji]
            if (weight != null) {
                totalWeightedFrequency += frequency * weight
                totalFrequency += frequency
            }
        }

        // Calculate the weighted average pleasantness level
        val averagePleasantness = totalWeightedFrequency / totalFrequency

        // Round the average pleasantness level to the nearest integer
        return averagePleasantness.roundToInt()
    }
}