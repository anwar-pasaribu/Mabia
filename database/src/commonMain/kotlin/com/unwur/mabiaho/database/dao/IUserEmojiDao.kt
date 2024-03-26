package com.unwur.mabiaho.database.dao

import com.unwur.mabiaho.database.domain.EmojiModel
import kotlinx.coroutines.flow.Flow

interface IUserEmojiDao {
    fun getUserEmojiByDate(date: Long): Flow<List<EmojiModel>>
    fun getUserEmojiByTimeStampRange(start: Long, end: Long): Flow<List<EmojiModel>>
    suspend fun getAllEmoji(): Flow<List<EmojiModel>>
    suspend fun insertEmoji(emojiUnicode: String, timestamp: Long)
}