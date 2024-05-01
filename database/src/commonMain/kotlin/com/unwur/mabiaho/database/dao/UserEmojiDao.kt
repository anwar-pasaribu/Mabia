package com.unwur.mabiaho.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.unwur.mabiaho.database.MabiaDatabase
import com.unwur.mabiaho.database.domain.EmojiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserEmojiDao(private val database: MabiaDatabase): IUserEmojiDao {

    override fun getUserEmojiByDate(date: Long): Flow<List<EmojiModel>> {
        return database.emojiQueries.selectEmojiByDate(date = date)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list ->
                list.map {
                    EmojiModel(it.id, it.emojiString, it.date)
                }
            }
    }

    override fun getUserEmojiByTimeStampRangeObservable(start: Long, end: Long): Flow<List<EmojiModel>> {
        return database.emojiQueries.selectEmojiByTimeStampRange(start, end)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    EmojiModel(it.id, it.emojiString, it.date)
                }
            }
    }

    override fun getUserEmojiByTimeStampRange(start: Long, end: Long): List<EmojiModel> {
        return database.emojiQueries.selectEmojiByTimeStampRange(start, end).executeAsList()
            .map {
                EmojiModel(it.id, it.emojiString, it.date)
            }
    }

    override suspend fun getAllEmoji(): Flow<List<EmojiModel>> {
        return database.emojiQueries.selectAllEmoji()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    EmojiModel(it.id, it.emojiString, it.date)
                }
            }
    }

    override suspend fun insertEmoji(emojiUnicode: String, timestamp: Long) {
        database.emojiQueries.insertNewEmoji(
            emojiUnicode,
            timestamp
        )
    }
}