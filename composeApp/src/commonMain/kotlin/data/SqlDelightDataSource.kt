package data

import com.unwur.mabiaho.database.dao.IUserEmojiDao
import kotlinx.datetime.Clock

interface ISqlDelightDataSource {
    suspend fun insertEmoji(emojiUnicode: String)
}

class SqlDelightDataSourceImpl(
    private val userEmojiDao: IUserEmojiDao  // TODO Maybe app not suppose to know DAO stuff
): ISqlDelightDataSource {
    override suspend fun insertEmoji(emojiUnicode: String) {
        val currentTimestamp = Clock.System.now().toEpochMilliseconds()
        userEmojiDao.insertEmoji(emojiUnicode, currentTimestamp)
    }
}