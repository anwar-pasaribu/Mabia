package ui.viewmodel

import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repo.KeyValueStorageRepository

class MainViewModel(
    private val kvsRepo: KeyValueStorageRepository
) : ViewModel() {

    fun saveFinishedOnboarding() {
        kvsRepo.saveOnboardingFinished()
    }

    fun onboardingFinished(): Boolean {
        return kvsRepo.onboardingFinished()
    }

    fun setMoodStateOnboardingSeen() {
        viewModelScope.launch {
            kvsRepo.moodRateLearned = true
        }
    }
}