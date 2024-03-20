package com.unwur.mabiaho.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneNotNull
import com.unwur.mabiaho.database.MabiaDatabase
import com.unwur.mabiaho.database.domain.EmojiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserEmojiDao(private val database: MabiaDatabase): IUserEmojiDao {
    override fun getUserEmojiByDate(date: Long): Flow<EmojiModel?> {
        return database.emojiQueries.selectEmojiByDate(
            date = date
        ).asFlow().mapToOneNotNull(Dispatchers.Default).map {
            EmojiModel(it.id, it.emojiString, it.date)
        }
    }

    override suspend fun insertEmoji(emojiUnicode: String, timestamp: Long) {
        database.emojiQueries.insertNewEmoji(
            emojiUnicode,
            timestamp
        )
    }
}