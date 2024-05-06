package ui.screen.onboarding

import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.KeyValueStorageRepository

class MoodRateViewModel(
    private val kvsRepo: KeyValueStorageRepository
) : ViewModel() {

    fun setMoodStateOnboardingSeen() {
        viewModelScope.launch {
            kvsRepo.moodRateLearned = true
        }
    }
}