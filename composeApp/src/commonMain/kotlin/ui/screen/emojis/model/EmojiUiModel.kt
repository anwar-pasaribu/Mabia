package ui.screen.emojis.model

import androidx.compose.runtime.Stable

@Stable
data class EmojiUiModel(
    val id: Int,
    val emojiUnicode: String,
    var selected: Boolean = false
)
