package data

import com.unwur.mabiaho.database.dao.IUserEmojiDao
import com.unwur.mabiaho.database.domain.EmojiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlinx.datetime.until

interface ISqlDelightDataSource {
    suspend fun insertEmoji(emojiUnicode: String)
    suspend fun getEmojiHistory(date: Long): Flow<List<EmojiModel>>
    suspend fun getEmojiHistoryRangeObservable(start: Long, end: Long): Flow<List<EmojiModel>>
    suspend fun getEmojiHistoryRange(start: Long, end: Long): List<EmojiModel>
    suspend fun getAllEmojiHistory(): Flow<List<EmojiModel>>
}

class SqlDelightDataSourceImpl(
    private val userEmojiDao: IUserEmojiDao  // TODO Maybe app not suppose to know DAO stuff
) : ISqlDelightDataSource {

    override suspend fun insertEmoji(emojiUnicode: String) {
        userEmojiDao.insertEmoji(emojiUnicode, Clock.System.now().toEpochMilliseconds())
    }

    override suspend fun getEmojiHistory(date: Long): Flow<List<EmojiModel>> {
        return userEmojiDao.getUserEmojiByDate(date)
    }

    override suspend fun getEmojiHistoryRangeObservable(
        start: Long,
        end: Long
    ): Flow<List<EmojiModel>> {
        return userEmojiDao.getUserEmojiByTimeStampRangeObservable(start, end)
    }

    override suspend fun getEmojiHistoryRange(start: Long, end: Long): List<EmojiModel> {
        return userEmojiDao.getUserEmojiByTimeStampRange(start, end)
    }

    override suspend fun getAllEmojiHistory(): Flow<List<EmojiModel>> {
        return userEmojiDao.getAllEmoji()
    }

    @Suppress("unused")
    private suspend fun generateRandomEmojiThisYearTillNow() = coroutineScope {
        launch(Dispatchers.IO) {
            val tz = TimeZone.currentSystemDefault()
            val today: LocalDate = Clock.System.todayIn(
                tz
            )
            val todayMonthNumber = today.monthNumber
            val localDateListThisYear = mutableListOf<LocalDate>()
            val currentTime = Clock.System.now().toLocalDateTime(tz).time
            (1..(todayMonthNumber)).forEach {
                val start = LocalDate(year = today.year, monthNumber = it, dayOfMonth = 1)
                val end = start.plus(1, DateTimeUnit.MONTH)
                val totalDayCountInTheMonth = start.until(end, DateTimeUnit.DAY)
                localDateListThisYear.add(
                    LocalDate(
                        year = today.year,
                        monthNumber = it,
                        dayOfMonth = totalDayCountInTheMonth
                    )
                )

                for (i in 1..totalDayCountInTheMonth) {
                    val date = LocalDate(year = today.year, monthNumber = it, dayOfMonth = i)
                    val dt = LocalDateTime(date = date, time = currentTime)
                    repeat(25) {
                        val randomEmoji = EmojiList.generateEmojiForUI().random()
                        userEmojiDao.insertEmoji(
                            randomEmoji.emojiUnicode,
                            dt.toInstant(tz).toEpochMilliseconds()
                        )
                    }
                }
            }
        }
    }

}