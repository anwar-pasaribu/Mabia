package ui.viewmodel

import data.EmojiList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.KeyValueStorageRepository
import ui.screen.emojis.model.EmojiUiModel

class EmojiListScreenViewModel(
    savedStateHolder: SavedStateHolder,
    keyValueStorageRepository: KeyValueStorageRepository,
): ViewModel() {

    val someSavedValue =
        MutableStateFlow(savedStateHolder.consumeRestored("someValue") as String? ?: "")

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())
    val showOnboardingBottomMenu = MutableStateFlow(!keyValueStorageRepository.onboardingFinished())

    init {
        savedStateHolder.registerProvider("someValue") {
            someSavedValue.value
        }
        viewModelScope.launch {
            emojiListStateFlow.emit(EmojiList.generateEmojiForUI())
        }
    }

    fun setOnboardingAlmostFinished() {
        showOnboardingBottomMenu.value = false
    }
}