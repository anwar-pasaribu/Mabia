package ui.viewmodel

import androidx.compose.ui.util.fastMap
import data.EmojiList
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.ISqlStorageRepository
import service.GenerativeAiService
import toComposeImageBitmap
import ui.screen.ai.ChatUiState
import ui.screen.ai.MutableChatUiState
import ui.screen.ai.model.ModelChatMessage
import ui.screen.ai.model.UserChatMessage
import ui.screen.emojis.model.EmojiUiModel
import kotlin.math.roundToInt

class HistoryScreenViewModel(
    private val sqlStorageRepository: ISqlStorageRepository,
    private val aiService: GenerativeAiService,
) : ViewModel() {

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())
    private val _moodRate = MutableStateFlow(EmojiList.MOOD_UNKNOWN)
    val moodRate: StateFlow<Int> = _moodRate.asStateFlow()

    private val _uiState = MutableChatUiState()
    val uiState: ChatUiState = _uiState

    private val emojiUnicodeList = hashMapOf<String, Int>()

    private val chat = aiService.startChat(
        history = listOf(
            content(role = "user") { text("Hello AI.") },
            content(role = "model") { text("Great to meet you.") },
        ),
    )

    fun loadAllEmoji() {
        viewModelScope.launch {
            sqlStorageRepository.getAllEmoji().collect { emojiList ->
                emojiListStateFlow.emit(emojiList.fastMap { emoji ->
                    EmojiUiModel(id = emoji.id.toInt(), emojiUnicode = emoji.emojiUnicode)
                })
            }
        }
    }

    fun getEmojiByTimeStampRange(untilTimeStampMillis: Long) {
        _uiState.canSendMessage = false
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
            delay(300)
            sqlStorageRepository
                .getEmojiByTimestampRange(startTimeStampMillis, untilTimeStampMillis)
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
                    _uiState.canSendMessage = emojiUnicodeList.isNotEmpty()
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
//        if (_uiState.canSendMessage && emojiUnicodeList.isNotEmpty()) {
//            try {
//                sendMessage(
//                    "$csv \n\n dari emoji itu, rate mood dari angka 1 sampe 7. " +
//                            "jawab dgn nomor 1 sampai 7. pastikan responmu hanya berupa nomor.",
//                )
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        } else {
//            _moodRate.value = calculateMoodRating(percentages)
//        }
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

    @Throws(dev.shreyaspatil.ai.client.generativeai.type.InvalidStateException::class)
    private fun sendMessage(prompt: String, image: ByteArray? = null) {
        val completeText = StringBuilder()

        val base = if (image != null) {
            aiService.generateContentWithVision(prompt, image)
        } else {
            chat.sendMessageStream(prompt)
        }

        val modelMessage = ModelChatMessage.LoadingModelMessage(
            base.map { it.text ?: "" }
                .onEach { completeText.append(it) }
                .onStart { _uiState.canSendMessage = false }
                .onCompletion {
                    _uiState.setLastModelMessageAsLoaded(completeText.toString())
                    _uiState.canSendMessage = true

                    println("GEMINI: $completeText")
                    var geminiRes = completeText.toString()
                    while (geminiRes.toIntOrNull() != null) {
                        val rateNumber = geminiRes.toInt()
                        geminiRes = ""
                        _moodRate.value = rateNumber
                    }

                    if (image != null) {
                        chat.history.add(content("user") { text(prompt) })
                        chat.history.add(content("model") { text(completeText.toString()) })
                    }
                }
                .catch {
                    _uiState.setLastMessageAsError(it.toString())
                    it.printStackTrace()
               },
        )

        viewModelScope.launch {
            _uiState.addMessage(UserChatMessage(prompt, image?.toComposeImageBitmap()))
            _uiState.addMessage(modelMessage)
        }
    }
}