package data

import com.unwur.mabiaho.database.dao.IUserEmojiDao
import com.unwur.mabiaho.database.domain.EmojiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

interface ISqlDelightDataSource {
    suspend fun insertEmoji(emojiUnicode: String)
    suspend fun getEmojiHistory(date: Long): Flow<List<EmojiModel>>
    suspend fun getEmojiHistoryRange(start: Long, end: Long): Flow<List<EmojiModel>>
    suspend fun getAllEmojiHistory(): Flow<List<EmojiModel>>
}

class SqlDelightDataSourceImpl(
    private val userEmojiDao: IUserEmojiDao  // TODO Maybe app not suppose to know DAO stuff
) : ISqlDelightDataSource {

    override suspend fun insertEmoji(emojiUnicode: String) {
        val currentTimestamp = Clock.System.now().toEpochMilliseconds()
        userEmojiDao.insertEmoji(emojiUnicode, currentTimestamp)
    }

    override suspend fun getEmojiHistory(date: Long): Flow<List<EmojiModel>> {
        return userEmojiDao.getUserEmojiByDate(date)
    }

    override suspend fun getEmojiHistoryRange(start: Long, end: Long): Flow<List<EmojiModel>> {
        return userEmojiDao.getUserEmojiByTimeStampRange(start, end)
    }

    override suspend fun getAllEmojiHistory(): Flow<List<EmojiModel>> {
        return userEmojiDao.getAllEmoji()
    }

}