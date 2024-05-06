package ui.screen.history

import androidx.compose.ui.util.fastMap
import data.EmojiList
import dev.shreyaspatil.ai.client.generativeai.type.content
import domain.usecase.GetEmojisByDateUseCase
import domain.usecase.GetMoodRateCalculationUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import kotlinx.datetime.until
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.ISqlStorageRepository
import service.GenerativeAiService
import toComposeImageBitmap
import ui.screen.ai.ChatUiState
import ui.screen.ai.MutableChatUiState
import ui.screen.ai.model.ModelChatMessage
import ui.screen.ai.model.UserChatMessage
import ui.screen.emojis.model.DayEmojiData
import ui.screen.emojis.model.EmojiUiModel
import ui.screen.emojis.model.MonthEmojiData


data class HistoryUiState(
    val items: List<EmojiUiModel> = emptyList(),
    val isLoading: Boolean = false,
)

class HistoryScreenViewModel(
    private val sqlStorageRepository: ISqlStorageRepository,
    private val aiService: GenerativeAiService,
    private val getEmojisByDateUseCase: GetEmojisByDateUseCase,
    private val getMoodRateCalculationUseCase: GetMoodRateCalculationUseCase,
) : ViewModel() {

    val selectedTimeStamp = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    private val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())
    val calenderListStateFlow = MutableStateFlow(emptyList<MonthEmojiData>())
    private val _moodRate = MutableStateFlow(EmojiList.MOOD_UNKNOWN)

    private val _uiState = MutableChatUiState()
    val uiState: ChatUiState = _uiState


    private val emojiUnicodeList = hashMapOf<String, Int>()

    private val chat = aiService.startChat(
        history = listOf(
            content(role = "user") { text("Hello AI.") },
            content(role = "model") { text("Great to meet you.") },
        ),
    )

    fun loadCalenderData() {
        viewModelScope.launch {

            withContext(Dispatchers.Default) {
                val tz = TimeZone.currentSystemDefault()
                val today: LocalDate = Clock.System.todayIn(tz)
                val calendarEmojis = mutableListOf<MonthEmojiData>()

                for (monthNum in 1..today.monthNumber) {

                    val start = LocalDate(year = today.year, monthNumber = monthNum, dayOfMonth = 1)
                    val thisMonth = today.monthNumber == monthNum
                    val end = if (thisMonth) {
                        start.plus(today.dayOfMonth, DateTimeUnit.DAY)
                    } else {
                        start.plus(1, DateTimeUnit.MONTH)
                    }
                    val totalDayCountInTheMonth = start.until(end, DateTimeUnit.DAY)

                    val dailyEmojiList = ArrayList<DayEmojiData>(totalDayCountInTheMonth)

                    for (day in start.dayOfMonth..totalDayCountInTheMonth) {

                        val dayLocalDate = LocalDate(
                            year = today.year,
                            monthNumber = monthNum,
                            dayOfMonth = day
                        )

                        val emojiListToday = getEmojisByDateUseCase.invoke(dayLocalDate)
                        val moodRate = getMoodRateCalculationUseCase.invoke(
                            emojiListToday.map { it.emojiUnicode }
                        )
                        val emojiByMoodRate = EmojiList.moodPleasantnessEmojiMapping[moodRate]
                        dailyEmojiList.add(
                            DayEmojiData(
                                day = dayLocalDate,
                                emoji = "$emojiByMoodRate",
                                moodRateValue = moodRate
                            )
                        )
                    }
                    calendarEmojis.add(
                        MonthEmojiData(
                            month = start,
                            dailyEmojis = dailyEmojiList.toImmutableList()
                        )
                    )
                }

                calenderListStateFlow.emit(calendarEmojis)
            }
        }

    }

    fun loadAllEmoji() {
        viewModelScope.launch {
            sqlStorageRepository.getAllEmoji().collect { emojiList ->
                emojiListStateFlow.emit(emojiList.fastMap { emoji ->
                    EmojiUiModel(id = emoji.id.toInt(), emojiUnicode = emoji.emojiUnicode)
                })
            }
        }
    }

    fun getEmojiByTimeStampRange(selectedLocalDate: LocalDate) {

        val tz = TimeZone.currentSystemDefault()
        val endOfTimeStampOfSelectedDate = LocalTime(hour = 23, minute = 59, second = 59)
        val dateTime = LocalDateTime(date = selectedLocalDate, time = endOfTimeStampOfSelectedDate)
        val untilTimeStampMillis = dateTime.toInstant(tz).toEpochMilliseconds()

        selectedTimeStamp.value = untilTimeStampMillis

        _uiState.canSendMessage = false

        viewModelScope.launch {
            _moodRate.value = EmojiList.MOOD_UNKNOWN
            emojiListStateFlow.emit(emptyList())
            emojiUnicodeList.clear()
            delay(300)

            val emojiUiModelList = getEmojisByDateUseCase.invoke(selectedLocalDate)
            emojiListStateFlow.emit(emojiUiModelList)
            _moodRate.value = getMoodRateCalculationUseCase.invoke(
                emojiUiModelList.map { it.emojiUnicode }
            )
            _uiState.canSendMessage = emojiUnicodeList.isNotEmpty()
        }
    }

    /**
     *          if (_uiState.canSendMessage && emojiUnicodeList.isNotEmpty()) {
     *             try {
     *                 sendMessage(
     *                     "$csv \n\n dari emoji itu, rate mood dari angka 1 sampe 7. " +
     *                             "jawab dgn nomor 1 sampai 7. pastikan responmu hanya berupa nomor.",
     *                 )
     *             } catch (e: Exception) {
     *                 e.printStackTrace()
     *             }
     *         } else {
     *             _moodRate.value = calculateMoodRating(percentages)
     *         }
     */
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