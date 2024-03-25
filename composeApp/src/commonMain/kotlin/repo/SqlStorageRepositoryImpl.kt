package repo

import data.ISqlDelightDataSource

interface ISqlStorageRepository {
    suspend fun saveEmoji(emojiUnicode: String)
}

class SqlStorageRepositoryImpl(private val sqlDelightDataSource: ISqlDelightDataSource): ISqlStorageRepository {
    override suspend fun saveEmoji(emojiUnicode: String) {
        sqlDelightDataSource.insertEmoji(emojiUnicode)
    }
}