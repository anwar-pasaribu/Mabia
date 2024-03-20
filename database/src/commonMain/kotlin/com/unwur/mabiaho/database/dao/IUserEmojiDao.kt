package com.unwur.mabiaho.database.dao

import com.unwur.mabiaho.database.domain.EmojiModel
import kotlinx.coroutines.flow.Flow

interface IUserEmojiDao {
    fun getUserEmojiByDate(date: Long): Flow<EmojiModel?>
    suspend fun insertEmoji(emojiUnicode: String, timestamp: Long)
}