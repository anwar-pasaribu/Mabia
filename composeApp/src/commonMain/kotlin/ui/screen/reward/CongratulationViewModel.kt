package ui.screen.reward

import moe.tlaster.precompose.viewmodel.ViewModel
import repo.KeyValueStorageRepository

class CongratulationViewModel(
    private val kvsRepo: KeyValueStorageRepository
) : ViewModel() {

    fun saveFinishedOnboarding() {
        kvsRepo.saveOnboardingFinished()
    }
}