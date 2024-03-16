package ui.screen.emojis.model

data class EmojiUiModel(
    val id: Int,
    val emojiUnicode: String,
    var selected: Boolean = false
)
