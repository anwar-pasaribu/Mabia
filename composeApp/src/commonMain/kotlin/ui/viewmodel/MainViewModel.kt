package ui.viewmodel

import moe.tlaster.precompose.viewmodel.ViewModel
import repo.KeyValueStorageRepository

class MainViewModel(
    private val keyValueStorageRepository: KeyValueStorageRepository
) : ViewModel() {

    fun saveFinishedOnboarding() {
        keyValueStorageRepository.saveOnboardingFinished()
    }

    fun onboardingFinished(): Boolean {
        return keyValueStorageRepository.onboardingFinished()
    }

}