package ui.viewmodel

import data.EmojiList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import ui.screen.emojis.model.EmojiUiModel

class EmojiListScreenViewModel(
    savedStateHolder: SavedStateHolder,
): ViewModel() {

    val someSavedValue =
        MutableStateFlow(savedStateHolder.consumeRestored("someValue") as String? ?: "")

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())

    init {
        savedStateHolder.registerProvider("someValue") {
            someSavedValue.value
        }
        viewModelScope.launch {
            delay(100)
            emojiListStateFlow.emit(EmojiList.generateEmojiForUI())
        }
    }

    fun setSomeValue(value: String) {
        someSavedValue.value = value
    }
}