package repo

import data.ILocalSetting

interface KeyValueStorageRepository {
    fun saveValue(key: String, value: Boolean)
    fun getValue(key: String): Boolean

    fun saveOnboardingFinished()
    fun onboardingFinished(): Boolean

    var moodRateLearned: Boolean
}

class KeyValueStorageRepositoryImpl(
    private val localSetting: ILocalSetting
): KeyValueStorageRepository {
    override fun saveValue(key: String, value: Boolean) {
        localSetting.saveBoolean(strKey = key, value = value)
    }

    override fun getValue(key: String): Boolean {
        return localSetting.getBoolean(strKey = key)
    }

    override fun saveOnboardingFinished() {
        localSetting.saveBoolean(KEY_ONBOARDING_FINISHED, true)
    }

    override fun onboardingFinished(): Boolean {
        return localSetting.getBoolean(KEY_ONBOARDING_FINISHED)
    }

    override var moodRateLearned: Boolean
        get() = localSetting.getBoolean(KEY_ONBOARDING_MOOD_RATE)
        set(value) { localSetting.saveBoolean(KEY_ONBOARDING_MOOD_RATE, value) }

    companion object {
        private const val KEY_ONBOARDING_FINISHED = "KEY_ONBOARDING_FINISHED"
        private const val KEY_ONBOARDING_MOOD_RATE = "KEY_ONBOARDING_MOOD_RATE"
    }

}