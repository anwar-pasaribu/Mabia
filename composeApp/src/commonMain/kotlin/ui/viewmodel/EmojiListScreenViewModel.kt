package ui.viewmodel

import data.EmojiList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.ISqlStorageRepository
import repo.KeyValueStorageRepository
import ui.screen.emojis.model.EmojiUiModel

class EmojiListScreenViewModel(
    private val keyValueStorageRepository: KeyValueStorageRepository,
    private val sqlStorageRepository: ISqlStorageRepository,
): ViewModel() {

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())
    val showOnboardingBottomMenu = MutableStateFlow(!keyValueStorageRepository.onboardingFinished())

    fun loadAllEmoji() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                emojiListStateFlow.emit(EmojiList.generateEmojiForUI().toImmutableList())
            }
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