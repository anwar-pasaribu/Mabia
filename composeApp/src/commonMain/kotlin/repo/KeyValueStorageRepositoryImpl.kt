package repo

import data.ILocalSetting

interface KeyValueStorageRepository {
    fun saveValue(key: String, value: Boolean)
    fun getValue(key: String): Boolean

    fun saveOnboardingFinished()
    fun onboardingFinished(): Boolean
}

class KeyValueStorageRepositoryImpl(private val localSetting: ILocalSetting): KeyValueStorageRepository {
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

    companion object {
        private const val KEY_ONBOARDING_FINISHED = "KEY_ONBOARDING_FINISHED"
    }

}