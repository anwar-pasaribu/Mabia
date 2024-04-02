package ui.viewmodel

import data.EmojiList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.ISqlStorageRepository
import repo.KeyValueStorageRepository
import ui.screen.emojis.model.EmojiUiModel

class EmojiListScreenViewModel(
    savedStateHolder: SavedStateHolder,
    private val keyValueStorageRepository: KeyValueStorageRepository,
    private val sqlStorageRepository: ISqlStorageRepository,
): ViewModel() {

    private val someSavedValue =
        MutableStateFlow(savedStateHolder.consumeRestored("someValue") as String? ?: "")

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())
    val showOnboardingBottomMenu = MutableStateFlow(!keyValueStorageRepository.onboardingFinished())

    init {
        savedStateHolder.registerProvider("someValue") {
            someSavedValue.value
        }
        viewModelScope.launch {
            emojiListStateFlow.emit(EmojiList.generateEmojiForUI().toImmutableList())
        }
    }

    fun getGreeting(): String {
        return if (!keyValueStorageRepository.onboardingFinished()) {
            greetingList().first()
        } else {
            greetingList().random()
        }
    }

    fun setOnboardingAlmostFinished() {
        showOnboardingBottomMenu.value = false
    }

    fun saveSelectedEmojiUnicode(emojiUnicode: String) {
        viewModelScope.launch {
            sqlStorageRepository.saveEmoji(emojiUnicode)
        }
    }

    private fun greetingList() = persistentListOf(
        "halo!",
        "gimana hari ini?",
        "all good?",
        "seru ga?",
        "hi!",
        "enjoy ga hari ini?",
        "aman?"
    )
}