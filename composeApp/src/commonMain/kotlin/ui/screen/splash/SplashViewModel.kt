package ui.screen.splash

import moe.tlaster.precompose.viewmodel.ViewModel
import repo.KeyValueStorageRepository

class SplashViewModel(
    private val kvsRepo: KeyValueStorageRepository
) : ViewModel() {

    fun onboardingFinished(): Boolean {
        return kvsRepo.onboardingFinished()
    }
}