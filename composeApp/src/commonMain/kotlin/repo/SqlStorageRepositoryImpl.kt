package repo

import com.unwur.mabiaho.database.domain.EmojiModel
import data.ISqlDelightDataSource
import kotlinx.coroutines.flow.Flow

interface ISqlStorageRepository {
    suspend fun saveEmoji(emojiUnicode: String)
    suspend fun getAllEmoji(): Flow<List<EmojiModel>>
    suspend fun getEmojiByTimestampRangeObservable(start: Long, end: Long): Flow<List<EmojiModel>>
    suspend fun getEmojiByTimestampRange(start: Long, end: Long): List<EmojiModel>
}

class SqlStorageRepositoryImpl(
    private val sqlDelightDataSource: ISqlDelightDataSource
): ISqlStorageRepository {

    override suspend fun saveEmoji(emojiUnicode: String) {
        sqlDelightDataSource.insertEmoji(emojiUnicode)
    }

    override suspend fun getAllEmoji(): Flow<List<EmojiModel>> {
        return sqlDelightDataSource.getAllEmojiHistory()
    }

    override suspend fun getEmojiByTimestampRangeObservable(start: Long, end: Long): Flow<List<EmojiModel>> {
        return sqlDelightDataSource.getEmojiHistoryRangeObservable(start, end)
    }

    override suspend fun getEmojiByTimestampRange(start: Long, end: Long): List<EmojiModel> {
        return sqlDelightDataSource.getEmojiHistoryRange(start, end)
    }
}