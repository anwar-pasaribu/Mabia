package ui.screen.emojis

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
    private val kvsRepo: KeyValueStorageRepository,
    private val sqlStorageRepository: ISqlStorageRepository,
): ViewModel() {

    private val shouldShowMoodRateOnboarding = kvsRepo.onboardingFinished() && !kvsRepo.moodRateLearned

    val emojiListStateFlow = MutableStateFlow(emptyList<EmojiUiModel>())
    val showOnboardingBottomMenu = MutableStateFlow(!kvsRepo.onboardingFinished())
    val showMoodRateForFirstTimer = MutableStateFlow(shouldShowMoodRateOnboarding)


    fun loadAllEmoji() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                emojiListStateFlow.emit(EmojiList.generateEmojiForUI().toImmutableList())
            }
        }
    }

    fun getGreeting(): String {
        return if (!kvsRepo.onboardingFinished()) {
            greetingList().first()
        } else {
            greetingList().random()
        }
    }

    fun setOnboardingAlmostFinished() {
        showOnboardingBottomMenu.value = false
        kvsRepo.saveOnboardingFinished()
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