package repo

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

interface LocalSetting {
    fun saveBoolean(strKey: String, value: Boolean)
    fun getBoolean(strKey: String): Boolean
}

class LocalSettingStorageImpl: LocalSetting {

    private val settings = Settings()

    override fun saveBoolean(strKey: String, value: Boolean) {
        settings[strKey] = value
    }

    override fun getBoolean(strKey: String): Boolean {
        return try {
            settings.getBoolean(strKey, false)
        } catch (e: Exception) {
            false
        }
    }
}